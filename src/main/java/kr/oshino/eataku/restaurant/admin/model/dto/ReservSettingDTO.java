package kr.oshino.eataku.restaurant.admin.model.dto;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservSettingDTO {

    private Long reservationNo;
    private RestaurantInfo restaurantNo;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int reservationPeople;
}
