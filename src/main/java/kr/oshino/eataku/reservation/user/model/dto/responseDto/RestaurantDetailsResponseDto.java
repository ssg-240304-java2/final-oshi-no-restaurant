package kr.oshino.eataku.reservation.user.model.dto.responseDto;


import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailsResponseDto {


    private Long restaurantNo;
    private LocalDateTime reservationTime;
    private int reservationPeople;

}
