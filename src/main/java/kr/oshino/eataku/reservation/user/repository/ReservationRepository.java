package kr.oshino.eataku.reservation.user.repository;

import kr.oshino.eataku.reservation.user.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {


    /* 예약 */
    @Query("SELECT r FROM Reservation r WHERE r.restaurantInfo.restaurantNo = :restaurantNo")
    List<Reservation> findReservationsByRestaurantNo(@Param("restaurantNo") Long restaurantNo);


    @Query("SELECT r.reservationTime FROM ReservationSetting r WHERE r.restaurantNo.restaurantNo = :restaurantNo")
    List<LocalDateTime> findTimesByRestaurantNo(@Param("restaurantNo") Long restaurantNo);



    @Query("SELECT r.reservationPeople FROM ReservationSetting r WHERE r.restaurantNo.restaurantNo = :restaurantNo")
    int findPeopleByRestaurantNo(@Param("restaurantNo") Long restaurantNo);

}
