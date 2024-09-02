package kr.oshino.eataku.restaurant.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_waiting_setting")
@ToString(exclude = {"restaurantNo"})
public class WaitingSetting {       // 웨이팅 설정

    @Id
    @Column(name = "waiting_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingNo;     // 웨이팅 고유 번호(pk)

    @ManyToOne()
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantNo;        // 식당 고유 번호(fk)

    @Column(name = "waiting_status")
    private String waitingStatus;       // 웨이팅 여부

    @Column(name = "waiting_people")
    private int waitingPeople;      // 웨이팅 인원

    @Column(name = "start_time")
    private Time startTime;        // 웨이팅 시작 시간

    @Column(name = "end_time")
    private Time endTime;      // 웨이팅 마감 시간

    @Column(name = "waiting_date")
    private Date waitingDate;      // 웨이팅 날짜
}
