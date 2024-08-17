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
            "( SELECT COUNT(*) AS cnt " +
            "FROM Member m " +
            "JOIN Reservation r ON m.memberNo = r.member.memberNo " +
            "JOIN Waiting w ON m.memberNo = w.member.memberNo " +
            "WHERE m.memberNo = :memberNo " +
            "AND ( r.reservationStatus =  kr.oshino.eataku.common.enums.ReservationStatus.방문완료 " +
            "OR w.waitingStatus = kr.oshino.eataku.common.enums.StatusType.방문완료 " +
            ")" +
            "GROUP BY m.memberNo " +
            ") AS a")
    String findBadgeByMemberNo(@Param("memberNo") Long logginedMemberNo);
}
