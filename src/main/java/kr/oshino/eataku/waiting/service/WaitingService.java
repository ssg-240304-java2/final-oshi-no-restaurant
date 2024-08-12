package kr.oshino.eataku.waiting.service;

import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.waiting.entity.Waiting;
import kr.oshino.eataku.waiting.model.dto.requestDto.CreateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.requestDto.ReadWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.CreateWaitingResponseDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import kr.oshino.eataku.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class WaitingService {

    private final WaitingRepository waitingRepository;
    // 여기에 member, restaurant 레포지토리 선언

    /**
     * 웨이팅 등록 비즈니스 로직
     * @param createWaitingRequestDto
     * @return
     */
    @Transactional
    public CreateWaitingResponseDto registerWaiting(CreateWaitingRequestDto createWaitingRequestDto) {

        // 여기 이전에 레스토랑에서 웨이팅 가능 여부를 확인해야 한다.

        // 멤버 객체와 레스토랑 객체 조회 후 객체 넣어줘야 한다.
        waitingRepository.save(Waiting.builder()
                .partySize(createWaitingRequestDto.getPartySize())
                .waitingStatus(StatusType.대기중)
                .build());

        // 이 부분 수정 필요
        return new CreateWaitingResponseDto(200, "웨이팅이 등록되었습니다!", 1);
    }

    /**
     * 웨이팅 조회 비즈니스 로직 (회원)
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
     * 웨이팅 조회 비즈니스 로직 (매장)
     * @param readWaitingRequestDto
     * @return
     */
    public List<ReadWaitingResponseDto> getWaitingListByRestaurantNo(ReadWaitingRequestDto readWaitingRequestDto) {
        return waitingRepository.findWaitingByRestaurantNo(readWaitingRequestDto.getRestaurantNo());
    }


}
