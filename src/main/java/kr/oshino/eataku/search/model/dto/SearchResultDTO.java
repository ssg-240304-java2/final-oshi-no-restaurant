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
    private Long inList;

    public SearchResultDTO(Long restaurantNo, String restaurantName, String restaurantAddress, Double xCoordinate, Double yCoordinate, String imgUrl, Double rating) {
        this.restaurantNo = restaurantNo;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.imgUrl = imgUrl;
        this.rating = rating;
    }
}
