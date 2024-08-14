package kr.oshino.eataku.list.model.repository;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.vo.RestaurantList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MyListRepository extends JpaRepository<MyList, Integer> {
    MyList findByListName(String listName);
    void deleteByListName(String ListName);

    MyList findByListNo(Integer listNo);

    @Query("SELECT r FROM MyList m JOIN m.restaurantList r")
    List<RestaurantList> findAllRestaurantCoordinates();
}


