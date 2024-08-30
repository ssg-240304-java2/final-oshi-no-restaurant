package kr.oshino.eataku.ws.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatMessageDTO {

    public enum MessageType {
        ENTER, TALK, EXIT, MATCH, MATCH_REQUEST
    }
    private Long chatMessageNo;
    private Long roomId;
    private String MemberNickname;
    private Long memberNo;
    private String message;
    private String imageUrl;
    private LocalDateTime sentAt;

}
