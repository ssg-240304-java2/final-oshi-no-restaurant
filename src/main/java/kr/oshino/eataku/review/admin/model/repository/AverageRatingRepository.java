package kr.oshino.eataku.review.admin.model.repository;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.review.admin.entity.AverageRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AverageRatingRepository extends JpaRepository<AverageRating, Long> {

    @Query("SELECT AVG(CAST(r.scope as int)) FROM Review r WHERE r.restaurantInfo.restaurantNo = :restaurantNo")
    double calculateAverageRatingByRestaurantNo(@Param("restaurantNo") Long restaurantNo);

    AverageRating findByRestaurantNo(RestaurantInfo restaurantInfo);
}
