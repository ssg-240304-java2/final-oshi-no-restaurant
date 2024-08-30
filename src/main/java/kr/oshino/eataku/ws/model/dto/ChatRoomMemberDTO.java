package kr.oshino.eataku.ws.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatRoomMemberDTO {

    private Long chatRoomMemberNo;
    private Long chatRoomNo;
    private Long memberNo;

}
