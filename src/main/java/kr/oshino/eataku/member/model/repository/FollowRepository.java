package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Follow;
import kr.oshino.eataku.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFromMemberNoAndToMemberNo(Member fromMember, Member toMember);

    int countByFromMemberNo(Member fromMember);

    int countByToMemberNo(Member toMember);

    void deleteByFromMemberNoAndToMemberNo(Member fromToMember, Member toToMember);
}
