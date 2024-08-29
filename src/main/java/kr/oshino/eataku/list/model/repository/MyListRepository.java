package kr.oshino.eataku.list.model.repository;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.dto.FollowListDto;
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
}


