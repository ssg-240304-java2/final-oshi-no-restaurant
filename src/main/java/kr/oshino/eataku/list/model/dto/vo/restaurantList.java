package kr.oshino.eataku.list.model.dto.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "list_resaurant") // 임시
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class restaurantList {

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
}
