package kr.oshino.eataku.list.model.repository;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.dto.FollowListDto;
import kr.oshino.eataku.list.model.dto.FollowerDTO;
import kr.oshino.eataku.list.model.dto.RestaurantWithRatingDTO;
import kr.oshino.eataku.list.model.vo.RestaurantList;
import kr.oshino.eataku.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyListRepository extends JpaRepository<MyList, Integer> {


    MyList findByListName(String listName);
    void deleteByListName(String ListName);

    MyList findByListNo(Integer listNo);

    @Query("SELECT r FROM MyList m JOIN m.restaurantList r")
    List<RestaurantList> findAllRestaurantCoordinates();

    List<MyList> findByListNameContainingAndListStatusOrderByListShareDesc(String query, String status);

    List<MyList> findByMemberAndListStatus(Member toMember, String status);

    int countByMemberAndListStatus(Member toMember, String status);

    @Query("SELECT ml " +
            "FROM MyList ml " +
            "JOIN Follow f ON f.toMemberNo = ml.member " +
            "WHERE f.fromMemberNo.memberNo = :memberNo")

    List<MyList> findByfollowMember(@Param("memberNo") Long loginedMemberNo);


    List<MyList> findByMemberMemberNo(Long loginedMemberNo);

    List<MyList> findAllByMemberMemberNo(Long memberNo);

    MyList findByMemberMemberNoAndListNo(Long memberNo, Integer listNo);

    @Query("SELECT new kr.oshino.eataku.list.model.dto.FollowerDTO( m.memberNo, " +
            "m.imgUrl, " +
            "m.nickname," +
            "COUNT(l.listNo) ) " +
            "FROM Member m " +
            "JOIN MyList l ON m.memberNo = l.member.memberNo " +
            "JOIN Follow f ON f.toMemberNo.memberNo = m.memberNo " +
            "WHERE f.fromMemberNo.memberNo = :mbrNo " +
            "GROUP BY m.memberNo")
    List<FollowerDTO> getFollowerList(@Param("mbrNo") Long mbrNo);

    @Query(value = "SELECT " +
            "r.restaurant_no, " +
            "r.restaurant_name," +
            "r.restaurant_address, " +
            "r.img_url, " +
            "r.x_coordinate, " +
            "r.y_coordinate, " +
            "ar.rating " +
            "FROM tbl_restaurant_info r " +
            "JOIN tbl_average_rating ar ON r.restaurant_no = ar.restaurant_no " +
            "JOIN tbl_user_list l ON r.restaurant_no = l.restaurant_no " +
            "WHERE l.list_no = :listNo ", nativeQuery = true)
    List<Object[]> getListRestaurants(@Param("listNo") Long listNo);
}


