package kr.oshino.eataku.waiting.model.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWaitingResponseDto {

    private int httpCode;
    private String message;
    private int memberNo;
}
