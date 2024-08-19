package kr.oshino.eataku.restaurant.admin.model.repository;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantInfo, Long> {
    RestaurantInfo findByAccountInfoId(String username);

    boolean existsByAccountInfoId(String account);

    RestaurantInfo save(RestaurantInfo restaurantInfo);

}
