package kr.oshino.eataku.ws.repository;

import kr.oshino.eataku.ws.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_RoomIdOrderBySendAtAsc(Long roomId);
}
