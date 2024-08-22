package kr.oshino.eataku.sse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseService {

    private final Map<Long, SseEmitter> sseWaitingEmitters = new ConcurrentHashMap<>();


    public SseEmitter subscribe(Long restaurantNo) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseWaitingEmitters.put(restaurantNo, sseEmitter);

        sseEmitter.onCompletion(() -> sseWaitingEmitters.remove(restaurantNo));
        sseEmitter.onTimeout(() -> sseWaitingEmitters.remove(restaurantNo));

        return sseEmitter;
    }



    public void sendWaitingRegisterEvent(Long restaurantNo) {

        SseEmitter sseEmitter = sseWaitingEmitters.get(restaurantNo);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("newWaiting").data(restaurantNo));
            } catch (Exception e) {
                sseWaitingEmitters.remove(restaurantNo);
            }
        }
    }
}
