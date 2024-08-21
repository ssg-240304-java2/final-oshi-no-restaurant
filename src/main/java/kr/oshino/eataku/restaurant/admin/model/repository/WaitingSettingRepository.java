package kr.oshino.eataku.restaurant.admin.model.repository;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.entity.WaitingSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface WaitingSettingRepository extends JpaRepository<WaitingSetting, Long> {

    WaitingSetting findByWaitingDateAndRestaurantNo(Date waitingDate, RestaurantInfo restaurantInfo);
}
