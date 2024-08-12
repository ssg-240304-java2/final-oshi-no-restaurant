package kr.oshino.eataku.waiting.service;

import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.waiting.entity.Waiting;
import kr.oshino.eataku.waiting.model.dto.requestDto.CreateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.CreateWaitingResponseDto;
import kr.oshino.eataku.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class WaitingService {

    private final WaitingRepository waitingRepository;
    // 여기에 member, restaurant 레포지토리 선언

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
}
