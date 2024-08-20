package kr.oshino.eataku.restaurant.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_waiting_setting")
public class WaitingSetting {       // 웨이팅 설정

    @Id
    @ManyToOne()
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantNo;        // 식당 고유 번호(fk)

    @Column(name = "waiting_status")
    private String waitingStatus;       // 웨이팅 여부

    @Column(name = "waiting_people")
    private int waitingPeople;      // 웨이팅 인원

    @Column(name = "start_time")
    private LocalTime startTime;        // 웨이팅 시작 시간

    @Column(name = "end_time")
    private LocalTime endTime;      // 웨이팅 마감 시간
}
