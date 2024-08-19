package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);

    Member findByMemberLoginInfoAccount(String account);

    Member findByMemberNo(Long logginedMemberNo);

    @Query( "SELECT CASE "+
    "WHEN a.cnt < 10 THEN '쩝쩝학생' " +
            "WHEN a.cnt < 50 THEN '쩝쩝학사'" +
            "WHEN a.cnt < 100 THEN '쩝쩝석사'" +
            "ELSE '쩝쩝박사' END " +
            "FROM " +
            "( SELECT SUM(l.listShare) AS cnt " +
            "FROM Member m " +
            "LEFT JOIN MyList l ON m.memberNo = l.member.memberNo " +
            "WHERE m.memberNo = :memberNo " +
            "GROUP BY m.memberNo " +
            ") AS a")
    String findBadgeByMemberNo(@Param("memberNo") Long logginedMemberNo);

    @Query(value = "SELECT img_url " +
            "FROM tbl_animal " +
            "WHERE weight < :weight " +
            "ORDER BY weight DESC " +
            "LIMIT 1", nativeQuery = true)
    String findAnimalImgUrlByWeight(@Param("weight") double weight);
}
