package kr.oshino.eataku.common.config;

import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SseConfig {

    @Bean
    public Map<Long, Sinks.Many<Long>> waitingSinks() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Sinks.Many<Long> waitingSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
