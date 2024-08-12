package kr.oshino.eataku.reservation.user.model.dto.requestDto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateReservationUserRequestDto {

    private int memberNo ;
    private int restaurantNo ;
    private int partySize;
    private LocalDate reservationDate;
    private LocalTime reservationTime;

}
