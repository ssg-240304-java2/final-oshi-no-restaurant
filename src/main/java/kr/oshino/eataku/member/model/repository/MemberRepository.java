package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);

    Member findByMemberLoginInfoAccount(String account);

    Member findByMemberNo(Long logginedMemberNo);

    @Query( "SELECT CASE "+
    "WHEN a.cnt < 10 THEN '쩝쩝학생' " +
            "WHEN a.cnt < 50 THEN '쩝쩝학사'" +
            "WHEN a.cnt < 100 THEN '쩝쩝석사'" +
            "ELSE '쩝쩝박사' END " +
            "FROM " +
            "( SELECT SUM(l.listShare) AS cnt " +
            "FROM Member m " +
            "LEFT JOIN MyList l ON m.memberNo = l.member.memberNo " +
            "WHERE m.memberNo = :memberNo " +
            "GROUP BY m.memberNo " +
            ") AS a")
    String findBadgeByMemberNo(@Param("memberNo") Long logginedMemberNo);

    @Query(value = "SELECT img_url " +
            "FROM tbl_animal " +
            "WHERE weight < :weight " +
            "ORDER BY weight DESC " +
            "LIMIT 1", nativeQuery = true)
    String findAnimalImgUrlByWeight(@Param("weight") double weight);

    @Query(value = "TRUNCATE TABLE tbl_busiest_restaurant ", nativeQuery = true)
    void truncateBusiestRestaurant();

    @Query(value = "TRUNCATE TABLE tbl_busiest_restaurant ", nativeQuery = true)
    void truncateListAddRestaurant();

    @Query(value = "INSERT INTO tbl_busiest_restaurant " +
            "SELECT r.restaurant_no " +
            "FROM ( " +
            "SELECT r. restaurant_no ," +
            "COUNT(w.member_no) AS wcnt, " +
            "COUNT(rsv.member_no) AS rcnt " +
            "FROM tbl_restaurant_info r " +
            "LEFT JOIN tbl_waiting_setting ws ON r.restaurant_no = ws.restaurant_no " +
            "LEFT JOIN tbl_waiting w ON r.restaurant_no = w.restaurant_no " +
            "LEFT JOIN tbl_reservation_setting rsvs ON r.restaurant_no = rsvs.restaurant_no " +
            "LEFT JOIN tbl_reservation rsv ON r.restaurant_no = rsv.restaurant_no " +
            "LEFT JOIN tbl_average_rating ar ON r.restaurant_no = ar.restaurant_no " +
            "WHERE ( ws.waiting_date = :today " +
            "AND ws.start_time < :time " +
            "AND ws.end_time > :time " +
            "AND ws.on_off = 'true' ) " +
            "OR ( rsvs.reservation_date = :today ) " +
            "HAVING wcnt <= 0 " +
            "AND rcnt <= 0 " +
            "ORDER BY ar.rating DESC " +
            ") r " , nativeQuery = true)
    void updateBusiestRestaurant();

    @Query(value = "INSERT INTO tbl_busiest_restaurant " +
            "SELECT r.restaurant_no " +
            "FROM ( " +
            "SELECT r. restaurant_no ," +
            "COUNT(w.member_no) AS wcnt, " +
            "COUNT(rsv.member_no) AS rcnt " +
            "FROM tbl_restaurant_info r " +
            "LEFT JOIN tbl_waiting_setting ws ON r.restaurant_no = ws.restaurant_no " +
            "LEFT JOIN tbl_waiting w ON r.restaurant_no = w.restaurant_no " +
            "LEFT JOIN tbl_reservation_setting rsvs ON r.restaurant_no = rsvs.restaurant_no " +
            "LEFT JOIN tbl_reservation rsv ON r.restaurant_no = rsv.restaurant_no " +
            "LEFT JOIN tbl_average_rating ar ON r.restaurant_no = ar.restaurant_no " +
            "WHERE ( ws.waiting_date = :today " +
            "AND ws.start_time < :time " +
            "AND ws.end_time > :time " +
            "AND ws.on_off = 'true' ) " +
            "OR ( rsvs.reservation_date = :today ) " +
            "HAVING wcnt <= 0 " +
            "AND rcnt <= 0 " +
            "ORDER BY ar.rating DESC " +
            ") r " , nativeQuery = true)
    void updateListAddRestaurant();

}
