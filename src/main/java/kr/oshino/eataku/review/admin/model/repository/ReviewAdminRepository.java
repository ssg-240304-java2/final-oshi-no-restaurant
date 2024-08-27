package kr.oshino.eataku.review.admin.model.repository;

import kr.oshino.eataku.review.user.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewAdminRepository extends JpaRepository<Review, Integer> {

    List<Review> findByRestaurantInfo_RestaurantNo(Long loginedRestaurantNo);
}
