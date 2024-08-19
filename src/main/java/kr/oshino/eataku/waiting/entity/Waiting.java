package kr.oshino.eataku.waiting.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.common.exception.exception.WaitingException;
import kr.oshino.eataku.common.exception.info.WaitingExceptionInfo;
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
    private Long waitingNo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column(name="party_size", nullable = false)
    private int partySize;

    @Column(name="waiting_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType waitingStatus = StatusType.대기중;

    @Column(name="sequence_number")
    private int sequenceNumber;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void cancel() {
        if(waitingStatus != StatusType.대기취소)
            this.waitingStatus = StatusType.대기취소;
        else
            throw new WaitingException(WaitingExceptionInfo.REQUEST_ALREADY_CANCELED);
    }

    public void visit() {
        if(waitingStatus != StatusType.방문완료)
            this.waitingStatus = StatusType.방문완료;
        else
            throw new WaitingException(WaitingExceptionInfo.REQUEST_ALREADY_HANDLED);
    }
}
