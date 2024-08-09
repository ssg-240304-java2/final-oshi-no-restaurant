package kr.oshino.eataku.waiting.service;

import kr.oshino.eataku.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;


}
