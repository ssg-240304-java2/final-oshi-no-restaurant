package kr.oshino.eataku.ws.model.dto;

import kr.oshino.eataku.ws.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResDTO {

    private Long memberNo;
    private String message;
    private String email;
    private LocalDateTime sentAt;

    public static ChatMessageResDTO fromEntity(ChatMessage chatMessage) {
        ChatMessageResDTO dto = new ChatMessageResDTO();
        if(chatMessage.getMember() != null) {
            dto.memberNo = chatMessage.getMember().getMemberNo();
            dto.email = chatMessage.getMember().getEmail();

        }else{
            dto.memberNo = chatMessage.getRestaurantInfo().getRestaurantNo();
            dto.email = chatMessage.getRestaurantInfo().getRestaurantName();
        }
        dto.message = chatMessage.getMessage();
        dto.sentAt = chatMessage.getSendAt();
        return dto;
    }
}
