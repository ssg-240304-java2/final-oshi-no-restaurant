package kr.oshino.eataku.list.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RestaurantWithRatingDTO {

    private Long restaurantNo;
    private String restaurantName;
    private String address;
    private String imgUrl;
    private Double xCoordinate;
    private Double yCoordinate;

    private Double rating;
}
