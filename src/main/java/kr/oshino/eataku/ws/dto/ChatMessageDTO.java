package kr.oshino.eataku.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatMessageDTO {

    public enum MessageType {
        ENTER, TALK, EXIT, MATCH, MATCH_REQUEST
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;

}
