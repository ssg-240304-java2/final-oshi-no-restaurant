package kr.oshino.eataku.ws.controller;

import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.ws.model.dto.ChatMessageDTO;
import kr.oshino.eataku.ws.model.dto.ChatRoomDTO;
import kr.oshino.eataku.ws.model.entity.ChatRoom;
import kr.oshino.eataku.ws.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) {
        log.info("🍎Received message: {}", message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    /***
     * 식당 채팅
     * @return
     */
    @GetMapping("restaurant/chatting")
    public String chattingView(Model model) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long restaurantNo = member.getRestaurantNo();

        model.addAttribute("userType", "restaurant");
        model.addAttribute("roomId", restaurantNo);
        log.info("🍎userType = " + model);
        return "ws/restaurant-chat";
    }

     // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestBody Map<String, String> data){ // {to:user01, from:user02}
        String user1 = data.get("to");
        String user2 = data.get("from");
        log.info("user1: {}, user2: {}", data.get("to"), data.get("from"));
        return chatRoomService.createChatRoom(user1, user2);
    }

    // 모든 채팅방 목록 반환
    @GetMapping("chat/roomList")
    @ResponseBody
    public List<ChatRoom> getChatRoomList() {

        return chatRoomService.findAllRooms();
    }

    // 채팅방 생성
    @PostMapping("chat/room")
    public ChatRoom createChatRoom(@RequestParam String user1, @RequestParam String user2){

        log.info("user1: {}, user2: {}", user1, user2);
        return chatRoomService.createChatRoom(user1, user2);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom getChatRoom(@PathVariable Long roomId) {
        return chatRoomService.findRoomById(roomId.toString());
    }



}
