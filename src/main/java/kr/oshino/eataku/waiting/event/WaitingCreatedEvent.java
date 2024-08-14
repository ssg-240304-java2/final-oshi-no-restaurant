package kr.oshino.eataku.waiting.event;

import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WaitingCreatedEvent extends ApplicationEvent {

    private final Long restaurantNo;

    public WaitingCreatedEvent(Object source, ReadWaitingResponseDto ReadWaitingResponseDto) {
        super(source);
        this.restaurantNo = ReadWaitingResponseDto.getRestaurantNo();
    }
}
