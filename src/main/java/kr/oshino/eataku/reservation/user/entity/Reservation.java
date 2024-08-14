package kr.oshino.eataku.reservation.user.entity;
import jakarta.persistence.*;

import kr.oshino.eataku.common.enums.AccountAuth;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="tbl_reservation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
public class Reservation {

    /*예약 번호*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_no")
    private Long reservationNo;


    /*회원 번호*/
    @ManyToOne(fetch = FetchType.LAZY)    // 아니면 eager?
    @JoinColumn(name="member_no")   // nullable =false
    private Member member;

    /*매장 번호*/
    @ManyToOne(fetch = FetchType.LAZY)   // 아니면 eager?
    @JoinColumn(name="restaurant_no")   // nullable = false
    private RestaurantInfo restaurantInfo;


    /*인원 수   를 어디에 보내야하나????*/
    @Column(name ="party_size")
    private int partySize;


    /*예약 상태 값*/
    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private AccountAuth.ReservationStatus reservationStatus;


    /*예약 날짜*/
    @Column(name = "reservation_date")
    private LocalDate reservationDate;


    /*예약 시간 */
    @Column(name = "reservation_time")
    private LocalTime reservationTime;


    /* 예약 생성 시간 */
    @Column(name = "created_time")
    @CreationTimestamp
    private LocalDateTime createdTime;


    /* 예약 상태 변화 시간 */
    @Column(name = "updated_time")
    @UpdateTimestamp
    private LocalDateTime updatedTime;


}
