package kr.oshino.eataku.reservation.user.service;
import kr.oshino.eataku.common.enums.ReservationStatus;
import kr.oshino.eataku.common.exception.exception.ReservationException;
import kr.oshino.eataku.common.exception.info.ReservationExceptionInfo;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.Notification;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.member.service.NotificationService;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.reservation.user.model.dto.requestDto.CreateReservationUserRequestDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.*;
import kr.oshino.eataku.reservation.user.repository.ReservationRepository;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.ReservationSettingRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class ReservationUserService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final NotificationService notificationService;
    private final ReservationSettingRepository reservationSettingRepository;


    @Transactional
    public CreateReservationUserResponseDto registerReservation(CreateReservationUserRequestDto createReservationUserRequestDto) {

        Member member = memberRepository.findById((long) createReservationUserRequestDto.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberNo: " + createReservationUserRequestDto.getMemberNo()));

        RestaurantInfo restaurantInfo = restaurantRepository.findById((long) createReservationUserRequestDto.getRestaurantNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurantNo: " + createReservationUserRequestDto.getRestaurantNo()));

        // 보류 매개변수 타입 확인 필요
        ReservationSetting reservationSetting = reservationSettingRepository.findByReservationDateAndReservationTimeAndRestaurantNo(
                createReservationUserRequestDto.getReservationDate(),
                createReservationUserRequestDto.getReservationTime(),
                restaurantInfo
        );


        // 비관적 락을 사용하여 동일 시간대에 내가 이미 예약했는지 확인하는 방법
        Optional<Reservation> existingReservation = reservationRepository.findFirstByRestaurantInfoAndReservationDateAndReservationTimeAndMember(
                restaurantInfo,
                createReservationUserRequestDto.getReservationDate(),
                createReservationUserRequestDto.getReservationTime(),
                member
        );

        System.out.println("existingReservation = " + existingReservation);

        // 동일 시간대에 내가 이미 예약했는지 확인하는 조건문
        if (existingReservation.isPresent()) {
            throw new ReservationException(ReservationExceptionInfo.DUPLICATED_RESERVATION);
        }
        // 자리가 남았는지 판별
        else if(reservationSetting.getReservationPeople() < createReservationUserRequestDto.getPartySize())
            throw new ReservationException(ReservationExceptionInfo.NO_SEATS_AVAILABLE);


        // 새로운 예약 생성
        Reservation reservation = Reservation.builder()
                .member(member)
                .restaurantInfo(restaurantInfo)
                .partySize(createReservationUserRequestDto.getPartySize())
                .reservationDate(createReservationUserRequestDto.getReservationDate())
                .reservationTime(createReservationUserRequestDto.getReservationTime())
                .reservationStatus(ReservationStatus.예약완료)
                .build();

        reservationRepository.save(reservation);
        // 인원 수 빼기
        reservationSetting.subtractPeople(createReservationUserRequestDto.getPartySize());
        reservationSettingRepository.save(reservationSetting);


        // partySize, time, reservation
//        reservationRepository.subtractPartySizeFromReservationPeople(
//                createReservationUserRequestDto.getPartySize(),
//                createReservationUserRequestDto.getReservationDate(),
//                createReservationUserRequestDto.getReservationTime(),
//                createReservationUserRequestDto.getRestaurantNo()
//        );

        // 알림등록 (+)
        Notification notification = Notification.builder()
                .toMember(reservation.getMember().getMemberNo())
                .type("reservation")
                .referenceNumber(reservation.getRestaurantInfo().getRestaurantNo())
                .message(notificationService.createNotificationMessage(
                        reservation.getRestaurantInfo().getRestaurantNo(),
                        "reservation"))
                .build();

        notificationService.insertNotification(notification);

            return new CreateReservationUserResponseDto(200, "예약 완료 되었습니다!",
                    restaurantInfo.getRestaurantName());
}



    /***
     * 해당 식당의 시간을 가져오는 메소드
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<Map<String, Object>> getAvailableTimeSlots(LocalDate date, Long restaurantNo, int partySize) {
        List<ReservationSetting> reservationSettings = reservationRepository.findAllByDateAndRestaurant(date, restaurantNo);

        return reservationSettings.stream()
                .map(setting -> {
                    Map<String, Object> timeSlot = Map.of(
                            "time", setting.getReservationTime(),
                            "isAvailable", partySize <= setting.getReservationPeople()
                    );
                    return timeSlot;
                })
                .collect(Collectors.toList());
    }


//    /**
//     * 인원수 차감 하는 메소드
//     *
//     * @param reservationNo
//     * @param partySize
//     * @param
//     */
//    @Transactional
//    public void subtractPartySize(Long reservationNo, int partySize, LocalTime time) {
//        reservationRepository.subtractPartySizeFromReservationPeople(partySize, time, reservationNo);
//    }


    /***
     * 상세정보가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public modalDto getModalDetails(Long restaurantNo) {
        Optional<Reservation> reservations = reservationRepository.findTopReservationByRestaurantNo(restaurantNo);

        if (reservations.isEmpty()) {
            throw new RuntimeException("예약정보가 없습니다. restaurantNo: " + restaurantNo);
        }

        Reservation reservation = reservations.get();
        System.out.println("reservation = " + reservation);

        return new modalDto(
                reservation.getRestaurantInfo().getRestaurantName(),
                reservation.getPartySize(),
                reservation.getReservationDate(),
                reservation.getReservationTime()
        );

    }

    /***
     * 날짜 가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<LocalDate> getAvailableDates(Long restaurantNo) {
        return reservationRepository.findAvailableDatesByRestaurantNo(restaurantNo);
    }


    /**
     * 방문 완료
     * 예약 조회 (회원)
     *
     * @param readReservationResponseDto
     * @return
     */
    @Transactional
    public List<ReadReservationResponseDto> getReservationListByMemberNo(ReadReservationResponseDto readReservationResponseDto) {
        
        List<ReadReservationResponseDto> reservationList = reservationRepository.findReservationByMemberNo(readReservationResponseDto.getMemberNo());
        System.out.println("reservationList = " + reservationList);

        if (readReservationResponseDto.getReservationStatus() != null) {
            reservationList = reservationList.stream()
                    .filter(dto -> dto.getReservationStatus().equals(readReservationResponseDto.getReservationStatus()))
                    .collect(Collectors.toList());
        }

        return reservationList;

    }


    /***
     * 예약 취소
     *
     */
    @Transactional
    public boolean cancelReservation(int reservationNo) {


        return reservationRepository.findById(reservationNo).map(reservation -> {
            LocalDate reservationDate = reservation.getReservationDate(); // 예약 날짜 가져오기
            LocalTime reservationTime = reservation.getReservationTime(); // 예약 시간 가져오기

            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            // 현재 날짜와 예약 날짜의 차이를 계산
            long daysUntilReservation = ChronoUnit.DAYS.between(currentDate, reservationDate);

            if (daysUntilReservation > 1 || (daysUntilReservation == 1 && currentTime.isBefore(reservationTime.minusHours(24)))) {
                // 예약 취소 처리
                reservationRepository.delete(reservation);

                // 취소한 인원수를 reservation_setting 테이블에 다시 추가
                updateAvailableSeats(reservation.getReservationNo(), reservation.getPartySize());

                // 알림등록 (+)
                Notification notification = Notification.builder()
                        .toMember(reservation.getMember().getMemberNo())
                        .type("reservationCancel")
                        .referenceNumber(reservation.getRestaurantInfo().getRestaurantNo())
                        .message(notificationService.createNotificationMessage(
                                reservation.getRestaurantInfo().getRestaurantNo(),
                                "reservationCancel"))
                        .build();

                notificationService.insertNotification(notification);
//                // 알림등록 (-)

                return true;
            } else {
                // 취소 불가
                return false;
            }
        }).orElse(false); // 해당 예약이 없는 경우

    }

    /**
     * 취소한 인원 만큼 증가하기
     *
     * @param reservationNo
     * @param partySize
     */
    @Transactional
    public void updateAvailableSeats(int reservationNo, int partySize) {
        reservationRepository.updateAvailableSeats(reservationNo, partySize);
    }


    /***
     * 식당 상세정보가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public RestaurantInfoDetails getRestaurantDetailsByReservation(Long restaurantNo) {
        return reservationRepository.findRestaurantDetailsByReservationNo(restaurantNo)
                .orElseThrow(() -> new IllegalArgumentException("없는 식당 " + restaurantNo));
    }

    /**
     * 식당의 리뷰 가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<ReviewDetails> getReviewDetails(Long restaurantNo) {
        return reservationRepository.getReviewDetails(restaurantNo);
    }

    /***
     * 태그 횟수 가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<String> getCountTags(Long restaurantNo) {
        return  reservationRepository.getCountTags(restaurantNo);
    }

    /***
     * 식당 지도 위치 가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<MapDto> getMapLocation(Long restaurantNo) {
        return reservationRepository.getMapLocation(restaurantNo);

    }

    /**
     * 식당 메뉴 사진
     * @param restaurantNo
     * @return
     */


    @Transactional
    public List<MenuDto> getMenu(Long restaurantNo) {

        RestaurantInfo restaurantInfo =restaurantRepository.findById(restaurantNo)
                .orElseThrow(() -> new IllegalArgumentException("없는 식당: " + restaurantNo));
        return reservationRepository.getMenu(restaurantInfo.getRestaurantNo());

    }

    /***
     * 식당 리뷰 사진 가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<ReviewImgDto> getImg(Long restaurantNo) {
        return reservationRepository.getImg(restaurantNo);
    }
}
