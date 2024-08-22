package kr.oshino.eataku.restaurant.admin.model.dto;

import jakarta.persistence.Column;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WaitingSettingDTO {

    private Long waitingNo;

    private Long restaurantNo;

    private String waitingStatus;

    private int waitingPeople;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate waitingDate;
}
