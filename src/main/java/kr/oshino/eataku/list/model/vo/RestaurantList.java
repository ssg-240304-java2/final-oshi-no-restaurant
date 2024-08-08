package kr.oshino.eataku.list.model.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantList {

    // 식당번호
    private Long restaurantNo;
    // 식당명
    private String restaurantName;
    // 식당 도로명 주소
    private String restaurantAddress;
    // 식당 사진
    private String imgUrl;
    // 식당 x 좌표
    private Double xCoordinate;
    // 식당 y 좌표
    private Double yCoordinate;
    // 식당 별점 통계
    private double rating;


}
