package kr.oshino.eataku.review.user.model.dto.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "review_restaurant") // 임시
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class reviewRestaurant {

    /* 매장 명? */
//    private Restaurant restaurant;
}
