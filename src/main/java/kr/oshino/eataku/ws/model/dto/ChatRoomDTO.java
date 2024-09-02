package kr.oshino.eataku.ws.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatRoomDTO {

    private Long roomId;
    private String name;
    private List<ChatRoomMemberDTO> chatRoomMembers;
    private List<ChatMessageDTO> chatMessages;

//    public static ChatRoomDTO create(String name) {
//        ChatRoomDTO chatRoom = new ChatRoomDTO();
//        chatRoom.roomId = UUID.randomUUID().toString();
//        return chatRoom;
//    }
}
