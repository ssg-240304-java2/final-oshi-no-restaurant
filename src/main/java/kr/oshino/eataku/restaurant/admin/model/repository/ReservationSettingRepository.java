package kr.oshino.eataku.restaurant.admin.model.repository;

import jakarta.persistence.LockModeType;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationSettingRepository extends JpaRepository<ReservationSetting, Long> {

    List<ReservationSetting> findByRestaurantNo(RestaurantInfo restaurantNo);


    List<ReservationSetting> findByReservationDateAndRestaurantNo(Date reservationDate, RestaurantInfo restaurantInfo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ReservationSetting findByReservationDateAndReservationTimeAndRestaurantNo
            (LocalDate reservationDate, LocalTime reservationTime, RestaurantInfo restaurantInfo);
}
