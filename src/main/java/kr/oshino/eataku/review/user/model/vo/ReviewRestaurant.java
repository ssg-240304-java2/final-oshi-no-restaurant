package kr.oshino.eataku.review.user.model.vo;

import kr.oshino.eataku.restaurant.admin.entity.Restaurant;
import lombok.*;

//@Entity
//@Table(name = "tbl_review_restaurant")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class ReviewRestaurant {

    /* 매장 명? */
    private Restaurant restaurant;
}
