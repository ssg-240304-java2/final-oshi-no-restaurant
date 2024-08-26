package kr.oshino.eataku.reservation.admin.repository;

import kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationAdminRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r.reservationDate" +
            ", COUNT(r.reservationNo) " +
            "FROM Reservation r " +
            "WHERE r.restaurantInfo.restaurantNo = :restaurantNo " +
            "AND MONTH(r.reservationDate) = :month " +
            "AND YEAR(r.reservationDate) = :year " +
            "GROUP BY r.reservationDate ")
    List<ReservationCountDTO> countRerservationsByMonth(@Param("month") int month, @Param("year") int year,@Param("restaurantNo") Long loginedRestaurantNo);
}
