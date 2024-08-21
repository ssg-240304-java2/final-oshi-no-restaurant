package kr.oshino.eataku.restaurant.admin.model.repository;

import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReservationSettingRepository extends JpaRepository<ReservationSetting, Long> {

    List<ReservationSetting> findByRestaurantNo(RestaurantInfo restaurantNo);


    List<ReservationSetting> findByReservationDateAndRestaurantNo(Date reservationDate, RestaurantInfo restaurantInfo);
}
