package kr.oshino.eataku.waiting.repository;

import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.waiting.entity.Waiting;
import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT new kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto(" +
            "w.waitingNo, w.partySize, w.waitingStatus, w.createdAt, w.updatedAt, " +
            "m.name, m.nickname, m.phone, r.restaurantNo, r.restaurantName) " +
            "FROM Waiting w " +
            "JOIN w.member m " +
            "JOIN w.restaurantInfo r " +
            "WHERE w.waitingNo = :waitingNo")
    ReadWaitingResponseDto findWaitingByWaitingNo(@Param("waitingNo") Long waitingNo);

    @Query("SELECT new kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto(" +
            "w.waitingNo, w.partySize, w.waitingStatus, w.createdAt, w.updatedAt, " +
            "m.name, m.nickname, m.phone, r.restaurantNo, r.restaurantName) " +
            "FROM Waiting w " +
            "JOIN w.member m " +
            "JOIN w.restaurantInfo r " +
            "WHERE m.memberNo = :memberNo")
    List<ReadWaitingResponseDto> findWaitingByMemberNo(@Param("memberNo") Long memberNo);


    @Query("SELECT new kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto(" +
            "w.waitingNo, w.partySize, w.waitingStatus, w.createdAt, w.updatedAt, " +
            "m.name, m.nickname, m.phone, r.restaurantNo, r.restaurantName) " +
            "FROM Waiting w " +
            "JOIN w.member m " +
            "JOIN w.restaurantInfo r " +
            "WHERE r.restaurantNo = :restaurantNo AND w.waitingStatus = kr.oshino.eataku.common.enums.StatusType.대기중")
    List<ReadWaitingResponseDto> findWaitingByRestaurantNo(@Param("restaurantNo") Long restaurantNo);

    Waiting findWaitingByMember_MemberNoAndWaitingStatus(Long logginedMemberNo, StatusType statusType);

    Optional<Waiting> findByMemberAndRestaurantInfoAndWaitingStatus(
            Member member, RestaurantInfo restaurantInfo, StatusType statusType
    );
}
