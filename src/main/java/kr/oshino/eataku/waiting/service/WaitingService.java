package kr.oshino.eataku.waiting.service;

import kr.oshino.eataku.common.enums.SmsMessageType;
import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.common.exception.exception.WaitingException;
import kr.oshino.eataku.common.exception.info.WaitingExceptionInfo;
import kr.oshino.eataku.common.util.BasicUtil;
import kr.oshino.eataku.common.util.SmsUtil;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.Notification;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.member.service.NotificationService;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.entity.WaitingSetting;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.WaitingSettingRepository;
import kr.oshino.eataku.sse.service.SseService;
import kr.oshino.eataku.waiting.entity.Waiting;
import kr.oshino.eataku.waiting.model.dto.requestDto.CreateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.requestDto.ReadWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.requestDto.UpdateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.CreateWaitingResponseDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.UpdateWaitingResponseDto;
import kr.oshino.eataku.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final WaitingSettingRepository waitingSettingRepository;
    private final NotificationService notificationService;

    private final SmsUtil smsUtil;
    private final BasicUtil basicUtil;
    private final SseService sseService;




    /**
     * 웨이팅 등록 (회원)
     * @param createWaitingRequestDto
     * @return
     */
    @Transactional
    public CreateWaitingResponseDto registerWaiting(CreateWaitingRequestDto createWaitingRequestDto) {


        Member member = memberRepository.findById(createWaitingRequestDto.getMemberNo())
                .orElseThrow(() -> new RuntimeException("해당하는 회원 정보가 없습니다!"));
        RestaurantInfo restaurantInfo = restaurantRepository.findById(createWaitingRequestDto.getRestaurantNo())
                .orElseThrow(() -> new RuntimeException("해당하는 레스토랑 정보가 없습니다!"));
        WaitingSetting waitingSetting = waitingSettingRepository.findByWaitingDateAndRestaurantNo(
                Date.valueOf(LocalDate.now()), restaurantInfo);



        // 동일 매장 중복 웨이팅 존재 여부 판별
        if(waitingRepository.findByMemberAndRestaurantInfoAndWaitingStatus(
                member, restaurantInfo, StatusType.대기중).isPresent()) {
            throw new WaitingException(WaitingExceptionInfo.DUPLICATED_WAITING);
        }

        // 매장의 웨이팅 정보 유무 판별 & 설정한 웨이팅 타임 판별
        if(waitingSetting == null) throw new WaitingException(WaitingExceptionInfo.WAITING_CLOSED);
        else if(!basicUtil.isCurrentTimeWithinSchedule(waitingSetting.getStartTime(), waitingSetting.getEndTime()))
            throw new WaitingException(WaitingExceptionInfo.WAITING_CLOSED);



        // 가장 최근의 순번을 가져와서 순번 결정
        Integer maxSequenceNumber = waitingRepository.findMaxSequenceNumberByRestaurantAndDate(restaurantInfo, LocalDate.now());
        int newSequenceNumber = (maxSequenceNumber != null) ? maxSequenceNumber + 1 : 1;

        Waiting waiting = waitingRepository.save(Waiting.builder()
                .partySize(createWaitingRequestDto.getPartySize())
                .restaurantInfo(restaurantInfo)
                .sequenceNumber(newSequenceNumber)
                .member(member)
                .waitingStatus(StatusType.대기중)
                .build());

        Long restaurantNo = waiting.getRestaurantInfo().getRestaurantNo();

        sseService.sendWaitingRegisterEvent(restaurantNo);

        // 알림등록
        Notification notification = Notification.builder()
                .toMember(waiting.getMember().getMemberNo())
                .type("waiting")
                .referenceNumber(waiting.getRestaurantInfo().getRestaurantNo())
                .message(notificationService.createNotificationMessage(
                        waiting.getRestaurantInfo().getRestaurantNo(),
                        "waiting"))
                .build();

        notificationService.insertNotification(notification);

        return new CreateWaitingResponseDto(200, "웨이팅이 등록되었습니다!", waiting.getWaitingNo(), member.getMemberNo());
    }






    /**
     * 웨이팅 조회 (회원)
     * @param readWaitingRequestDto
     * @return
     */
    public List<ReadWaitingResponseDto> getWaitingListByMemberNo(ReadWaitingRequestDto readWaitingRequestDto) {

        List<ReadWaitingResponseDto> waitingList = waitingRepository.findWaitingByMemberNo(readWaitingRequestDto.getMemberNo());

        // 요청 dto 의 Waiting Status 에 따라 해당 정보만 반환
        if(readWaitingRequestDto.getWaitingStatus() != null) {
            waitingList = waitingList.stream()
                    .filter(dto -> dto.getWaitingStatus().equals(readWaitingRequestDto.getWaitingStatus()))
                    .collect(Collectors.toList());
        }

        return waitingList;
    }





    /**
     * 웨이팅 조회 (매장)
     * @param readWaitingRequestDto
     * @return
     */
    public List<ReadWaitingResponseDto> getWaitingListByRestaurantNo(ReadWaitingRequestDto readWaitingRequestDto) {
        return waitingRepository.findWaitingByRestaurantNo(readWaitingRequestDto.getRestaurantNo());
    }





    /**
     * 웨이팅 취소 (매장, 회원)
     * @param updateWaitingRequestDto
     * @return
     */
    @Transactional
    public UpdateWaitingResponseDto cancelWaitingByWaitingNo(UpdateWaitingRequestDto updateWaitingRequestDto) {

        Long waitingNo = updateWaitingRequestDto.getWaitingNo();
        Waiting waiting = waitingRepository.findById(waitingNo).orElseThrow(() -> new RuntimeException("해당하는 웨이팅 정보가 없습니다!"));
        waiting.cancel();
        waitingRepository.save(waiting);

        // 여기서 카카오톡 알림 메세지 전송

        // 알림등록
        Notification notification = Notification.builder()
                .toMember(waiting.getMember().getMemberNo())
                .type("waitingCancel")
                .referenceNumber(waiting.getRestaurantInfo().getRestaurantNo())
                .message(notificationService.createNotificationMessage(
                        waiting.getRestaurantInfo().getRestaurantNo(),
                        "waiting"))
                .build();

        notificationService.insertNotification(notification);

        return new UpdateWaitingResponseDto(200, "웨이팅 대기가 취소되었습니다!");
    }





    /**
     * 웨이팅 방문 처리 (매장)
     * @param updateWaitingRequestDto
     * @return
     */
    @Transactional
    public UpdateWaitingResponseDto updateWaitingByWaitingNo(UpdateWaitingRequestDto updateWaitingRequestDto) {
        Long waitingNo = updateWaitingRequestDto.getWaitingNo();
        Waiting waiting = waitingRepository.findById(waitingNo).orElseThrow(() -> new RuntimeException("해당하는 웨이팅 정보가 없습니다!"));
        waiting.visit();
        waiting.getMember().increaseWeight(3.0);
        waitingRepository.save(waiting);

        // 알림등록 (+)
        Notification notification = Notification.builder()
                .toMember(waiting.getMember().getMemberNo())
                .type("review")
                .referenceNumber(waiting.getRestaurantInfo().getRestaurantNo())
                .serviceType("waiting")
                .serviceNo(waiting.getWaitingNo())
                .message(notificationService.createNotificationMessage(
                        waiting.getRestaurantInfo().getRestaurantNo(),
                        "review"))
                .build();

        notificationService.insertNotification(notification);
        // 알림등록 (-)

        return new UpdateWaitingResponseDto(200, "웨이팅 대기가 방문 처리 되었습니다!");
    }


    // ----------------------------------------------------------------------------------------------------------------


    /**
     * 웨이팅 입장 메세지 전송
     * @param waitingNo
     * @return
     */
    public SingleMessageSentResponse sendWaitingEntryMessage(Long waitingNo) {

        ReadWaitingResponseDto waitingData = waitingRepository.findWaitingByWaitingNo(waitingNo);
        return smsUtil.sendWaitingMessage(waitingData.getPhone(), SmsMessageType.WAITING_ENTRY_MESSAGE,
                waitingData.getSequenceNumber(), waitingData.getRestaurantName(), waitingData.getPartySize());
    }






    /**
     * 웨이팅 등록 메세지 전송
     * @param waitingNo
     * @return
     */
    public SingleMessageSentResponse sendWaitingRegisterMessage(Long waitingNo) {

        ReadWaitingResponseDto waitingData = waitingRepository.findWaitingByWaitingNo(waitingNo);
        return smsUtil.sendWaitingMessage(waitingData.getPhone(), SmsMessageType.WAITING_REGISTER_MESSAGE,
                waitingData.getRestaurantName(), waitingData.getSequenceNumber(), waitingData.getPartySize());
    }
}

