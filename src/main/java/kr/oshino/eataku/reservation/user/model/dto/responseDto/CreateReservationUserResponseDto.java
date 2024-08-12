package kr.oshino.eataku.reservation.user.model.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationUserResponseDto {

    private int httpCode;
    private String message;
    private int memberNo;

}
