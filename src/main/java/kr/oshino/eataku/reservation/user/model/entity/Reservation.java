package kr.oshino.eataku.reservation.user.model.entity;
import jakarta.persistence.*;
//import kr.oshino.eataku.waiting.user.model.Restaurant;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="Reservation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Reservation {

    /*예약 번호*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int reservation_no;

    /*회원 번호*/
    @Column(name = "member_no")
    private int memberNO;

    /*매장 번호*/

//    @Column(name = "restaurant_no")
//    @EmbeddedId
//    private Restaurant restaurantNo;

    /*인원 수*/
    @Column(name ="party_size")
    private int partySize;

    /*예약 상태 값*/
    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    /*예약 날짜*/
    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    /*예약 시간 */
    @Column(name = "reservation_time")
    private LocalTime reservationTime;

    /* 예약 생성 시간 */
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    /* 예약 상태 변화 시간 */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;


}
