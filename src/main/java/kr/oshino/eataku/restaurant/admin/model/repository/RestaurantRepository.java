package kr.oshino.eataku.restaurant.admin.model.repository;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.waiting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantInfo, Long> {
}
