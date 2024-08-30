package kr.oshino.eataku.ws.service;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.ws.entity.ChatMessage;
import kr.oshino.eataku.ws.entity.ChatRoomMember;
import kr.oshino.eataku.ws.model.dto.ChatMessageDTO;
import kr.oshino.eataku.ws.model.dto.ChatRoomDTO;
import kr.oshino.eataku.ws.entity.ChatRoom;
import kr.oshino.eataku.ws.repository.ChatMessageRepository;
import kr.oshino.eataku.ws.repository.ChatRoomMemberRepository;
import kr.oshino.eataku.ws.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final RestaurantRepository restaurantRepository;
    private Map<String, ChatRoomDTO> chatRoomMap;

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;


//    public List<ChatRoom> findAllRooms() {
//        return chatRoomRepository.findAll();
//    }

//    public ChatRoom createChatRoom(String user1, String user2) {
//        String roomName = user1 + " & " + user2;
//        ChatRoom chatRoom = ChatRoom.create(roomName);
//        return chatRoomRepository.save(chatRoom);
//    }


//    public ChatRoom findRoomById(String roomName) {
//        return chatRoomRepository.findById(roomName).orElse(null);
//    }

    public List<ChatRoom> findByRestaurantNo(Long restaurantNo) {
        return chatRoomRepository.findByRestaurantInfoRestaurantNo(restaurantNo);
    }

    public ChatRoom createRoom(RestaurantInfo restaurantInfo, Member member) {
        String chatRoomName = Optional.ofNullable(member.getName().toString()).orElseThrow(() -> new IllegalArgumentException("restaurantNo is required"));
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .member(member)
                .restaurantInfo(restaurantInfo)
                .messages(new ArrayList<>())
                .build();

        return chatRoomRepository.save(chatRoom);
    }


    public void addMember(Long roomId, Long currentUserId, Long restaurantId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        Member member = memberRepository.findById(currentUserId).orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + currentUserId));
        RestaurantInfo restaurantInfo = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restaurantId));

        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .restaurantInfo(restaurantInfo)
                .build();

        chatRoomMemberRepository.save(chatRoomMember);
    }

    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    public Optional<ChatRoom> findRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId);
    }


    public ChatMessage createAndSaveChatMessage(ChatMessageDTO chatMessageDTO, Long currentUser) {
        Long chatRoomNo = chatMessageDTO.getRoomId();

        // 채팅방 번호로 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방을 찾을 수 없습니다."));

        Member member = memberRepository.findById(currentUser).orElseThrow();

        // ChatMessage 객체 생성
        ChatMessage chatMessage = ChatMessage.createChatMessage(chatMessageDTO, chatRoom, member);

        // 채팅방에 메시지 추가
        chatRoom.addMessage(chatMessage);

        // 생성된 메시지를 저장하고 반환
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByChatRoom_RoomIdOrderBySendAtAsc(roomId);
    }

    public ChatRoom findByRestaurantInfoAndMember(RestaurantInfo restaurantInfo, Member member) {
        return chatRoomRepository.findByRestaurantInfoAndMember(restaurantInfo, member);
    }
}
