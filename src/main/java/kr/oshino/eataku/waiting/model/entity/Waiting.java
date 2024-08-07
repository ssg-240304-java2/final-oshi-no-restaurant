package kr.oshino.eataku.waiting.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="Waiting")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Waiting {

    @Id
    @Column(name="waiting_no")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int waitingNo;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name = "restaurant_no", nullable = false)
//    private Restaurant restaurant;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_no", nullable = false)
//    private Member member;

    @Column(name="party_size", nullable = false)
    private int partySize;

    @Column(name="waiting_status", nullable = false)
    private StatusType waitingStatus;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
