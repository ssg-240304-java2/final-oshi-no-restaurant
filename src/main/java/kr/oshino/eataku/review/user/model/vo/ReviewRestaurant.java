package kr.oshino.eataku.review.user.model.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReviewRestaurant {

    /* 식당 번호 */
    private Long restaurantNo;

    /* 식당 명*/
    private String restaurantName;

    /* 도로명 주소*/
    private String restaurantAddress;

    /* 식당 사진*/
    private String imgUrl;


}
