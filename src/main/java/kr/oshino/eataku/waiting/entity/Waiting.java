package kr.oshino.eataku.waiting.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_waiting")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
public class Waiting {

    @Id
    @Column(name="waiting_no")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int waitingNo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "restaurant_no", nullable = false)
    private RestaurantInfo restaurantInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(name="party_size", nullable = false)
    private int partySize;

    @Column(name="waiting_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType waitingStatus;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
