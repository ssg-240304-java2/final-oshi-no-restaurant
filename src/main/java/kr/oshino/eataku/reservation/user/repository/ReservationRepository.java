package kr.oshino.eataku.reservation.user.repository;

import jakarta.transaction.Transactional;

import kr.oshino.eataku.common.enums.ReservationStatus;

import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.ReadReservationResponseDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.RestaurantInfoDetails;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.ReviewDetails;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {


    /***
     * 예약 등록하는 메소드
     * @param restaurantNo
     * @return
     */
    /* 예약 */
    @Query("SELECT r FROM Reservation r WHERE r.restaurantInfo.restaurantNo = :restaurantNo")
    List<Reservation> findReservationsByRestaurantNo(@Param("restaurantNo") Long restaurantNo);


    /***
     * 해당 하는 식당의 시간을 가져오는 메소드
     * @param restaurantNo
     * @return
     */
    @Query(value = "SELECT r.reservationTime FROM ReservationSetting r WHERE r.restaurantNo.restaurantNo = :restaurantNo")
    List<java.sql.Time> findAllTimesByRestaurantNo(@Param("restaurantNo") Long restaurantNo);




    /***
     * 해당 하는 식당의 인원수를 가져오는 메소드
     * @param restaurantNo
     * @return
     */
    @Query("SELECT r.reservationPeople FROM ReservationSetting r WHERE r.restaurantNo.restaurantNo  = :restaurantNo")
    int findPeopleByRestaurantNo(@Param("restaurantNo") Long restaurantNo);


    /**
     * 해당 인원 수를 제거하는 메소드
     * @param partySize
     * @param
     */
    @Modifying
    @Query("UPDATE ReservationSetting r SET r.reservationPeople = r.reservationPeople - :partySize " +
            "WHERE r.reservationTime = :time AND  r.restaurantNo.restaurantNo = :restaurantNo AND r.reservationPeople > 0")
    void subtractPartySizeFromReservationPeople(@Param("partySize") int partySize,
                                                @Param("time") LocalTime time,
                                                @Param("restaurantNo") Long restaurantNo);



    @Query("SELECT r FROM ReservationSetting r WHERE r.reservationDate = :date AND r.restaurantNo.restaurantNo = :restaurantNo")
    List<ReservationSetting> findAllByDateAndRestaurant(@Param("date") LocalDate date, @Param("restaurantNo") Long restaurantNo);



    /***
     * 식당 상제정보 가져오기
     * @param restaurantNo
     * @return
     */
    @Query("SELECT r FROM Reservation r WHERE r.restaurantInfo.restaurantNo = :restaurantNo ORDER BY r.reservationNo DESC LIMIT 1")
    Optional<Reservation> findTopReservationByRestaurantNo(@Param("restaurantNo") Long restaurantNo);


    /**
     * 영업
     * 날짜 가져오기
     */
    @Query("SELECT DISTINCT CAST(r.reservationDate AS java.time.LocalDate) FROM ReservationSetting r WHERE r.restaurantNo.restaurantNo = :restaurantNo")
    List<LocalDate> findAvailableDatesByRestaurantNo(@Param("restaurantNo") Long restaurantNo);


    /***
     * 예약 정보 조회
     * @param memberNo
     * @return
     */
    @Query("SELECT new kr.oshino.eataku.reservation.user.model.dto.responseDto.ReadReservationResponseDto( " +
            "r.reservationNo, r.partySize, r.reservationStatus, " +
            "m.name , m.nickname , m.phone, m.memberNo, ri.restaurantName) " +
            "FROM Reservation r " +
            "JOIN r.member m " +
            "JOIN r.restaurantInfo ri " +
            "WHERE m.memberNo = :memberNo")
    List<ReadReservationResponseDto> findReservationByMemberNo(Long memberNo);





    /***
     * 예약취소 관련 메소드
     */
    @Modifying
    @Transactional
    @Query("UPDATE ReservationSetting rs SET rs.reservationPeople = rs.reservationPeople+ :partySize WHERE rs.reservationNo = :reservationNo")
    void updateAvailableSeats(int reservationNo, int partySize);




    /**
     * 회원 예약 상태 조회
     * @param memberNo
     * @param status
     * @return
     */
    List<Reservation> findByMember_MemberNoAndReservationStatusIn(Long memberNo, ReservationStatus[] status);


    /***
     * 상세정보
     */
    @Query("SELECT new kr.oshino.eataku.reservation.user.model.dto.responseDto.RestaurantInfoDetails(" +
            "r.restaurantName, r.restaurantAddress,r.imgUrl ) " +
            "FROM RestaurantInfo r " +
            "WHERE r.restaurantNo = :restaurantNo")
    Optional<RestaurantInfoDetails> findRestaurantDetailsByReservationNo(@Param("restaurantNo") Long restaurantNo);


    /**
     * 식당의 리뷰 가져오기
     * @param restaurantNo
     * @return
     */
    @Query("SELECT new kr.oshino.eataku.reservation.user.model.dto.responseDto.ReviewDetails( "  +
            "m.name , re.reviewContent ) " +
            "FROM Review re " +
            "JOIN re.member m " +
            "JOIN re.restaurantInfo r " +
            "WHERE r.restaurantNo= :restaurantNo")
    List<ReviewDetails> getReviewDetails(@Param("restaurantNo") Long restaurantNo);
}



