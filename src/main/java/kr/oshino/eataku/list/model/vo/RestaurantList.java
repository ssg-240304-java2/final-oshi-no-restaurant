package kr.oshino.eataku.list.model.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;

@Embeddable
@Data
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantList that = (RestaurantList) o;
        return Objects.equals(restaurantNo, that.restaurantNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantNo);
    }
}
