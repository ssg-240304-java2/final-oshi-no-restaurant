package kr.oshino.eataku.ws.controller;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.ws.entity.ChatMessage;
import kr.oshino.eataku.ws.model.dto.ChatMessageDTO;
import kr.oshino.eataku.ws.entity.ChatRoom;
import kr.oshino.eataku.ws.model.dto.ChatMessageResDTO;
import kr.oshino.eataku.ws.repository.ChatRoomRepository;
import kr.oshino.eataku.ws.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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

    private final ChatRoomService chatRoomService;


//    @MessageMapping("/chat/message")
//    public void message(ChatMessageDTO message) {
//        log.info("🍎Received message: {}", message);
//        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }

    // 채팅방 조회
    @GetMapping("/restaurant/chatting")
    public String getChatList(Model model) {

        log.info("==== get chat list ====");
        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long restaurantNo = member.getRestaurantNo();

        List<ChatRoom> chatRoom = chatRoomService.findByRestaurantNo(restaurantNo);
        model.addAttribute("rooms", chatRoom);
        model.addAttribute("sender", restaurantNo);

        log.info("🍎 [ ChatController ]  chatRoom = {}", chatRoom);

        return "ws/restaurant-chat";
    }




}
