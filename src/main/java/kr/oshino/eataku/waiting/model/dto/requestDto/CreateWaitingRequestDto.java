package kr.oshino.eataku.waiting.model.dto.requestDto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWaitingRequestDto {

    private Long memberNo;
    private Long restaurantNo;
    private int partySize;
}
