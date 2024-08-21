package kr.oshino.eataku.sse.controller;

import kr.oshino.eataku.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/sse")
@Slf4j
public class SseController {

    private final SseService sseService;


    @GetMapping(value = "/waiting/{restaurantNo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable Long restaurantNo) {

        log.info("======== SSE Controller Access =========");
        log.info("restaurantNo: {}", restaurantNo);

        SseEmitter sseEmitter = sseService.subscribe(restaurantNo);
        sseService.sendWaitingRegisterEvent(restaurantNo);

        log.info("======== SSE Controller Access for restaurantNo: {} at {} =========", restaurantNo, LocalDateTime.now());
        return sseEmitter;
    }
}
