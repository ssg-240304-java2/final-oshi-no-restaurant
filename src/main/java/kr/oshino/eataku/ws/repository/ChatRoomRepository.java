package kr.oshino.eataku.ws.repository;

import jakarta.annotation.PostConstruct;
import kr.oshino.eataku.ws.dto.ChatRoomDTO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoomDTO> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRoom() {
        List chatRoooms = new ArrayList(chatRoomMap.values());
        Collections.reverse(chatRoooms);
        return chatRoooms;
    }

    public ChatRoomDTO createChatRoom(String user1, String user2) {

        String roomName = user1 + " & " + user2;
        ChatRoomDTO chatRoom = ChatRoomDTO.create(roomName);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public ChatRoomDTO findRoomById(String roomId) {
        return chatRoomMap.get(roomId);
    }
}
