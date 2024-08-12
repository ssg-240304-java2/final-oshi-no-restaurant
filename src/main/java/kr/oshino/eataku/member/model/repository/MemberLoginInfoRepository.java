package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.MemberLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLoginInfoRepository extends JpaRepository<MemberLoginInfo, Member> {
    boolean existsByAccount(String account);
}
