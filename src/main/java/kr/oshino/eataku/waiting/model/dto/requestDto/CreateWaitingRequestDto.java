package kr.oshino.eataku.waiting.model.dto.requestDto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWaitingRequestDto {

    private Long memberNo;
    private Integer restaurantNo;
    private int partySize;
}
