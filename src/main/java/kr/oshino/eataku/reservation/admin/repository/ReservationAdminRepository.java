package kr.oshino.eataku.reservation.admin.repository;

import kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationDTO;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationAdminRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT new kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO(r.reservationDate " +
            ", COUNT(r.reservationNo) ) " +
            "FROM Reservation r " +
            "WHERE r.restaurantInfo.restaurantNo = :restaurantNo " +
//            "AND MONTH(r.reservationDate) = :month " +
//            "AND YEAR(r.reservationDate) = :year " +
            "AND r.reservationStatus = '예약완료'" +
            "GROUP BY r.reservationDate ")
    List<ReservationCountDTO> countReservationsByMonth(@Param("restaurantNo") Long loginedRestaurantNo);



    @Query("SELECT new kr.oshino.eataku.reservation.admin.model.dto.ReservationDTO(" +
            "r.reservationNo, m.memberNo, m.name, m.phone, " +
            "rest.restaurantNo, r.partySize, r.reservationDate, r.reservationTime)" +
            "FROM Reservation r " +
            "JOIN r.member m " +
            "JOIN r.restaurantInfo rest " +
            "WHERE rest.restaurantNo = :loginedRestaurantNo " +
            "AND r.reservationDate = :date " +
            "AND r.reservationStatus = '예약완료'")
    List<ReservationDTO> findByReservationDateAndRestaurantInfo(LocalDate date, Long loginedRestaurantNo);

//    List<ReservationCountDTO> countRerservationsByMonth(@Param("month") int month, @Param("year") int year,@Param("restaurantNo") Long loginedRestaurantNo);
}
