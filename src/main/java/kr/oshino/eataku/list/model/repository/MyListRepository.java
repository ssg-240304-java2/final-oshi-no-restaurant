package kr.oshino.eataku.list.model.repository;

import kr.oshino.eataku.list.entity.MyList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyListRepository extends JpaRepository<MyList, Integer> {
}
