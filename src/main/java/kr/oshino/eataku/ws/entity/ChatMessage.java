package kr.oshino.eataku.ws.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.ws.model.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.annotation.CheckForNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_chat_message")
@Builder
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_no")
    private Long chatMessageNo;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @Column(name = "message")
    private String message;

    @CreationTimestamp
    private LocalDateTime sendAt;

    public static ChatMessage createChatMessage(ChatMessageDTO chatMessageDTO, ChatRoom chatRoom, Member currentUser) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(currentUser)
                .message(chatMessageDTO.getMessage())
                .sendAt(chatMessageDTO.getSentAt())
                .build();
    }
}
