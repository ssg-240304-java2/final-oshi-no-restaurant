package kr.oshino.eataku.ws.service;

import jakarta.annotation.PostConstruct;
import kr.oshino.eataku.ws.model.dto.ChatRoomDTO;
import kr.oshino.eataku.ws.model.entity.ChatRoom;
import kr.oshino.eataku.ws.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private Map<String, ChatRoomDTO> chatRoomMap;

    private final ChatRoomRepository chatRoomRepository;


    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom createChatRoom(String user1, String user2) {
        String roomName = user1 + " & " + user2;
        ChatRoom chatRoom = ChatRoom.create(roomName);
        return chatRoomRepository.save(chatRoom);
    }


    public ChatRoom findRoomById(String roomName) {
        return chatRoomRepository.findById(roomName).orElse(null);
    }
}
