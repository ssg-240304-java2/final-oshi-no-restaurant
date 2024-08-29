package kr.oshino.eataku.ws.repository;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.ws.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomId(Long restaurantNo);

    ChatRoom findByRestaurantInfoAndMember(RestaurantInfo restaurantInfo, Member member);

    List<ChatRoom> findByRestaurantInfoRestaurantNo(Long restaurantNo);
}
