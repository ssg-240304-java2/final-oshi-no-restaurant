package kr.oshino.eataku.search.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SearchResultDTO {

    private Long restaurantNo;
    private String restaurantName;
    private String restaurantAddress;
    private Double xCoordinate;
    private Double yCoordinate;
    private String imgUrl;
    private String foodType;
    private String hashTag;
    private Double rating;
}
