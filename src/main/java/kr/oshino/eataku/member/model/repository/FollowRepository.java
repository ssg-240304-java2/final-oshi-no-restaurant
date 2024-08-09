package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
