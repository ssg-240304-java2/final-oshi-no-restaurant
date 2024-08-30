package kr.oshino.eataku.ws.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_chat_room")
@Builder
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantInfo;

    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();


    public static ChatRoom create(String roomName) {

        return ChatRoom.builder().name(roomName).build();
    }

    public void addMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
    }
}
