package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);

    Member findByMemberLoginInfoAccount(String account);
}
