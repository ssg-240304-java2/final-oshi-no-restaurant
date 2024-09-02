package kr.oshino.eataku.review.admin.entity;


import jakarta.persistence.*;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.review.user.entity.Review;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_average_rating")
public class AverageRating {

    @Id
    @Column(name = "rating_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingNo;      // 별점 고유 번호

    @OneToOne()
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantNo;        // 식당 고유 번호

    @Column(name = "rating")
    private double averageRating;      // 별점 평균

}
