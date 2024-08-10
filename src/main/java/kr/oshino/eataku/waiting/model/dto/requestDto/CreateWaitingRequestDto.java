package kr.oshino.eataku.waiting.model.dto.requestDto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWaitingRequestDto {

    private int memberNo;
    private int restaurantNo;
    private int partySize;
}
