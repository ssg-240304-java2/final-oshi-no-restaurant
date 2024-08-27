package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);

    Member findByMemberLoginInfoAccount(String account);

    Member findByMemberNo(Long logginedMemberNo);

    @Query( "SELECT CASE "+
    "WHEN a.cnt < 10 THEN '쩝쩝학생' " +
            "WHEN a.cnt < 50 THEN '쩝쩝학사'" +
            "WHEN a.cnt < 100 THEN '쩝쩝석사'" +
            "WHEN a.cnt >= 100 THEN '쩝쩝박사'" +
            "ELSE '쩝쩝학생' END " +
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

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE tbl_busiest_restaurant ", nativeQuery = true)
    void truncateBusiestRestaurant();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE tbl_popular_restaurant ", nativeQuery = true)
    void truncateListAddRestaurant();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE tbl_direct_restaurant ", nativeQuery = true)
    void truncateDirectRestaurant();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tbl_busiest_restaurant " +
            "SELECT total.restaurant_no, SUM(total.total_sum) AS total_sum " +
            "      FROM (SELECT COALESCE(r.restaurant_no, w.restaurant_no) AS restaurant_no, " +
            "                   COALESCE(r.sum, 0) + COALESCE(w.sum, 0) AS total_sum " +
            "              FROM (SELECT r.restaurant_no, SUM(r.party_size) AS sum " +
            "                      FROM tbl_reservation r " +
            "                     WHERE r.reservation_date = CURDATE() " +
            "                       AND r.reservation_status = '예약중' " +
            "                     GROUP BY r.restaurant_no) r " +
            "                     LEFT JOIN (SELECT w.restaurant_no, SUM(w.party_size) AS sum " +
            "                                  FROM tbl_waiting w " +
            "                                 WHERE w.waiting_status = '대기중' " +
            "                                 GROUP BY w.restaurant_no) w " +
            "                     ON r.restaurant_no = w.restaurant_no " +
            "             UNION " +
            "            SELECT COALESCE(r.restaurant_no, w.restaurant_no) AS restaurant_no, " +
            "                   COALESCE(r.sum, 0) + COALESCE(w.sum, 0) AS total_sum " +
            "              FROM (SELECT r.restaurant_no, SUM(r.party_size) AS sum " +
            "                      FROM tbl_reservation r " +
            "                     WHERE r.reservation_date = CURDATE() " +
            "                       AND r.reservation_status = '예약중' " +
            "                     GROUP BY r.restaurant_no) r " +
            "                     RIGHT JOIN (SELECT w.restaurant_no, SUM(w.party_size) AS sum " +
            "                                   FROM tbl_waiting w " +
            "                                  WHERE w.waiting_status = '대기중' " +
            "                                  GROUP BY w.restaurant_no) w " +
            "                     ON r.restaurant_no = w.restaurant_no ) total " +
            "GROUP BY total.restaurant_no ",
            nativeQuery = true)
    void updateBusiestRestaurant();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tbl_popular_restaurant " +
            "SELECT ul.restaurant_no, " +
            "       COUNT(ul.restaurant_no) AS share " +
            "FROM tbl_user_list ul " +
            "GROUP BY ul.restaurant_no ", nativeQuery = true)
    void updateListAddRestaurant();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tbl_direct_restaurant " +
            "SELECT result.restaurant_no " +
            "FROM ( " +
            "         SELECT ws.restaurant_no, " +
            "                COUNT(w.member_no) cnt " +
            "           FROM tbl_waiting_setting ws " +
            "                    LEFT JOIN tbl_waiting w ON w.restaurant_no = ws.restaurant_no " +
            "          WHERE ws.waiting_date = CURDATE() " +
            "          GROUP BY ws.restaurant_no " +
            "          HAVING cnt <= 0 " +
            "       ) result", nativeQuery = true)
    void updateDirectRestaurant();

    @Query(value = "SELECT restaurant_no " +
            "FROM tbl_popular_restaurant", nativeQuery = true)
    List<Long> selectPopularRestaurants();

    @Query(value = "SELECT restaurant_no " +
            "FROM tbl_busiest_restaurant ", nativeQuery = true)
    List<Long> selectBusiestRestaurants();

    @Query(value = "SELECT restaurant_no " +
            "FROM tbl_direct_restaurant ", nativeQuery = true)
    List<Long> selectDirectRestaurants();

    Member findByNameAndEmail(String name, String email);

    Member findByMemberLoginInfoAccountAndNameAndEmail(String id, String name, String email);

    boolean existsByMemberLoginInfoAccountAndNameAndEmail(String id, String name, String email);

    @Query("SELECT m.name FROM Member m WHERE m.memberNo = :referenceNo")
    String findNameByMemberNo(@Param("referenceNo") Long referenceNo);
}
