package kr.oshino.eataku.reservation.user.model.dto.responseDto;

import kr.oshino.eataku.common.enums.AccountAuth;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReadReservationResponseDto {

    // Reservation
    private int reservationNo;

    private AccountAuth reservationStatus;
    private int partySize;


    //Member Entity 정보
    private String name;
    private String nickname;
    private String phone;
    private Long memberNo;
    // Restaurant_Info Entity 정보
    private String restaurantName;



}
