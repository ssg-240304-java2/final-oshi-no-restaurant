package kr.oshino.eataku.restaurant.admin.model.repository;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;

import kr.oshino.eataku.restaurant.admin.model.dto.SalesDTO;
import kr.oshino.eataku.search.model.dto.SearchResultDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantInfo, Long> {
    RestaurantInfo findByAccountInfoId(String username);

    boolean existsByAccountInfoId(String account);
    RestaurantInfo save(RestaurantInfo restaurantInfo);

    @Query(value = "SELECT r.restaurant_no, " +
            "r.restaurant_name, " +
            "r.restaurant_address, " +
            "r.x_coordinate, " +
            "r.y_coordinate, " +
            "r.img_url, " +
            "GROUP_CONCAT(DISTINCT CONCAT('#', ft.food_type) ORDER BY ft.food_type ASC SEPARATOR ' ') AS food_type, " +
            "GROUP_CONCAT(DISTINCT CONCAT('#', ht.hash_tag) ORDER BY ht.hash_tag ASC SEPARATOR ' ') AS hash_tag, " +
            "ar.rating " +
            "FROM tbl_restaurant_info r " +
            "LEFT JOIN tbl_food_type ft ON r.restaurant_no = ft.restaurant_no " +
            "LEFT JOIN tbl_menu m ON m.restaurant_no = r.restaurant_no " +
            "LEFT JOIN tbl_hash_tag ht ON ht.restaurant_no = r.restaurant_no " +
            "LEFT JOIN tbl_average_rating ar ON ar.restaurant_no = r.restaurant_no " +
            "WHERE (:reservation IS NULL " +
            "OR r.restaurant_no IN ( SELECT rs.restaurant_no " +
            "                           FROM tbl_reservation_setting rs " +
            "                           JOIN tbl_reservation rv " +
            "                            ON rs.restaurant_no = rv.restaurant_no AND rs.reservation_date = rv.reservation_date" +
            "                         GROUP BY rs.restaurant_no, rs.reservation_date, rs.reservation_people" +
            "                          HAVING rs.reservation_people > SUM(rv.party_size) ) ) " +
            "AND (:waiting IS NULL " +
            "OR r.restaurant_no IN ( SELECT ws.restaurant_no FROM tbl_waiting_setting ws WHERE ws.waiting_date = CURDATE()) ) " +
            "AND (r.restaurant_name LIKE CONCAT('%', :keyword, '%') " +
            "OR r.restaurant_address LIKE CONCAT('%', :keyword, '%') " +
            "OR r.description LIKE CONCAT('%', :keyword, '%') " +
            "OR m.menu_name LIKE CONCAT('%', :keyword, '%') " +
            "OR m.description LIKE CONCAT('%', :keyword, '%') " +
            "OR ht.hash_tag LIKE CONCAT('%', :keyword, '%')) " +
            "AND (ft.food_type IN (:categories) ) " +
            "GROUP BY r.restaurant_no, r.restaurant_name, r.restaurant_address, " +
            "r.x_coordinate, r.y_coordinate, r.img_url",
            countQuery = "SELECT COUNT(*) " +
                    "FROM tbl_restaurant_info r " +
                    "LEFT JOIN tbl_food_type ft ON r.restaurant_no = ft.restaurant_no " +
                    "LEFT JOIN tbl_menu m ON m.restaurant_no = r.restaurant_no " +
                    "LEFT JOIN tbl_hash_tag ht ON ht.restaurant_no = r.restaurant_no " +
                    "LEFT JOIN tbl_average_rating ar ON ar.restaurant_no = r.restaurant_no " +
                    "WHERE (:reservation IS NULL " +
                    "OR r.restaurant_no IN ( SELECT rs.restaurant_no " +
                    "                           FROM tbl_reservation_setting rs " +
                    "                           LEFT JOIN tbl_reservation rv " +
                    "                            ON rs.restaurant_no = rv.restaurant_no AND rs.reservation_date = rv.reservation_date" +
                    "                         GROUP BY rs.restaurant_no, rs.reservation_date, rs.reservation_people" +
                    "                          HAVING rs.reservation_people > SUM(rv.party_size) ) ) " +
                    "AND (:waiting IS NULL " +
                    "OR r.restaurant_no IN ( SELECT ws.restaurant_no FROM tbl_waiting_setting ws WHERE ws.waiting_date = CURDATE()) ) " +
                    "AND (r.restaurant_name LIKE CONCAT('%', :keyword, '%') " +
                    "OR r.restaurant_address LIKE CONCAT('%', :keyword, '%') " +
                    "OR r.description LIKE CONCAT('%', :keyword, '%') " +
                    "OR m.menu_name LIKE CONCAT('%', :keyword, '%') " +
                    "OR m.description LIKE CONCAT('%', :keyword, '%') " +
                    "OR ht.hash_tag LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (ft.food_type IN (:categories) ) ",
            nativeQuery = true)
    List<Object[]> findAllByKeywordAndCategories(@Param("keyword") String keyword,
                                                 @Param("categories") List<String> categories,
                                                 @Param("reservation") String reservation,
                                                 @Param("waiting") String waiting,
                                                 Pageable pageable);





    @Query(value = "SELECT result.restaurant_no, " +
            "result.restaurant_name, " +
            "result.restaurant_address, " +
            "result.x_coordinate, " +
            "result.y_coordinate, " +
            "result.img_url, " +
            "result.food_type, " +
            "result.hash_tag, " +
            "result.rating " +
            "FROM ( SELECT (6371 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(r.x_coordinate)) " +
            "* COS(RADIANS(r.y_coordinate) - RADIANS(:lng)) " +
            "+ SIN(RADIANS(:lat)) * SIN(RADIANS(r.x_coordinate)))) AS distance, " +
            "r.restaurant_no, " +
            "r.restaurant_name, " +
            "r.restaurant_address, " +
            "r.x_coordinate, " +
            "r.y_coordinate, " +
            "r.img_url, " +
            "GROUP_CONCAT(DISTINCT CONCAT('#', ft.food_type) ORDER BY ft.food_type ASC SEPARATOR ' ') AS food_type, " +
            "GROUP_CONCAT(DISTINCT CONCAT('#', ht.hash_tag) ORDER BY ht.hash_tag ASC SEPARATOR ' ') AS hash_tag, " +
            "ar.rating " +
            "FROM tbl_restaurant_info r " +
            "LEFT JOIN tbl_food_type ft ON r.restaurant_no = ft.restaurant_no " +
            "LEFT JOIN tbl_menu m ON m.restaurant_no = r.restaurant_no " +
            "LEFT JOIN tbl_hash_tag ht ON ht.restaurant_no = r.restaurant_no " +
            "LEFT JOIN tbl_average_rating ar ON ar.restaurant_no = r.restaurant_no " +
            "WHERE ( r.restaurant_name LIKE CONCAT('%',:keyword,'%') " +
            "OR r.restaurant_address LIKE CONCAT('%',:keyword,'%') " +
            "OR r.description LIKE CONCAT('%',:keyword,'%') " +
            "OR ft.food_type LIKE CONCAT('%',:keyword,'%') " +
            "OR m.menu_name LIKE CONCAT('%',:keyword,'%') " +
            "OR m.description LIKE CONCAT('%',:keyword,'%') " +
            "OR ht.hash_tag LIKE CONCAT('%',:keyword,'%') ) " +
            "GROUP BY r.restaurant_no, r.restaurant_name, r.restaurant_address, ar.rating " +
            "HAVING distance <= 0.5 " +
            "ORDER BY ar.rating DESC ) result " +
            "LIMIT 30 ",
            nativeQuery = true)
    List<Object[]> selectQueryBylatitudeAndlongitude(@Param("lat") Double latitude,@Param("lng") Double longitude, @Param("keyword") String keyword);


    @Query("SELECT new kr.oshino.eataku.search.model.dto.SearchResultDTO(r.restaurantNo, r.restaurantName, r.restaurantAddress, r.xCoordinate, r.yCoordinate, r.imgUrl, ar.averageRating ) " +
            "FROM RestaurantInfo r LEFT JOIN AverageRating ar ON r.restaurantNo = ar.restaurantNo.restaurantNo " +
            "WHERE r.restaurantNo IN :popularList")
    List<SearchResultDTO> findPopularRestaurantsWithRatings(@Param("popularList") List<Long> popularList);

    @Query("SELECT r.restaurantName FROM RestaurantInfo r WHERE r.restaurantNo = :referenceNo")
    String findRestaurantNameByRestaurantNo(@Param("referenceNo") Long referenceNo);

    RestaurantInfo findByRestaurantNo(Long restaurantNo);

    @Query(value = "SELECT total.restaurant_no as restaurantNo, " +
            "total.serviceType as serviceType, " +
            "total.party_size as partySize, " +
            "total.date as date, " +
            "total.time as time, " +
            "total.name as name, " +
            "total.contact as contact, " +
            "total.count as count " +
            "FROM (" +
            "   SELECT rv.restaurant_no, " +
            "          '예약' as serviceType, " +
            "          rv.party_size, " +
            "          DATE_FORMAT(rv.updated_time, '%Y-%m-%d') as date, " +
            "          DATE_FORMAT(rv.updated_time, '%H:%i') as time, " +
            "          (SELECT m.name FROM tbl_member m WHERE rv.member_no = m.member_no) as name, " +
            "          (SELECT m.phone FROM tbl_member m WHERE rv.member_no = m.member_no) as contact, " +
            "          (SELECT ( " +
            "              (SELECT COUNT(*) FROM tbl_reservation cnt WHERE cnt.member_no = rv.member_no AND cnt.restaurant_no = :restaurantNo) " +
            "              + " +
            "              (SELECT COUNT(*) FROM tbl_waiting cnt WHERE cnt.member_no = rv.member_no AND cnt.restaurant_no = :restaurantNo) " +
            "          )) as count " +
            "     FROM tbl_reservation rv " +
            "    WHERE rv.restaurant_no = :restaurantNo " +
            "      AND rv.updated_time BETWEEN :startDay and :endDay " +
            "    UNION ALL " +
            "    SELECT wt.restaurant_no, " +
            "           '웨이팅' as serviceType, " +
            "           wt.party_size, " +
            "           DATE_FORMAT(wt.updated_at, '%Y-%m-%d') as date, " +
            "           DATE_FORMAT(wt.updated_at, '%H:%i') as time, " +
            "           (SELECT m.name FROM tbl_member m WHERE wt.member_no = m.member_no) as name, " +
            "           (SELECT m.phone FROM tbl_member m WHERE wt.member_no = m.member_no) as contact, " +
            "           (SELECT ( " +
            "              (SELECT COUNT(*) FROM tbl_reservation cnt WHERE cnt.member_no = wt.member_no AND cnt.restaurant_no = :restaurantNo) " +
            "              + " +
            "              (SELECT COUNT(*) FROM tbl_waiting cnt WHERE cnt.member_no = wt.member_no AND cnt.restaurant_no = :restaurantNo) " +
            "           )) as count " +
            "     FROM tbl_waiting wt " +
            "    WHERE wt.restaurant_no = :restaurantNo " +
            "      AND wt.updated_at BETWEEN :startDay and :endDay " +
            ") total " +
            "ORDER BY total.date DESC, total.time DESC",
            nativeQuery = true)
    List<Object[]> findTotalStatisticsByRestaurantNo(@Param("restaurantNo") Long loginedRestaurantNo,
                                                     @Param("startDay") LocalDate startDay,
                                                     @Param("endDay") LocalDate endDay);
}
