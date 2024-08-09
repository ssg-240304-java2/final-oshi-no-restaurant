package kr.oshino.eataku.waiting.repository;

import kr.oshino.eataku.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Integer> {


}
