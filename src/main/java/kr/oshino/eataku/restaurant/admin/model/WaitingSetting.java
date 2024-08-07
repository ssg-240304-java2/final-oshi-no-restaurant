package kr.oshino.eataku.restaurant.admin.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalTime;

@Entity
@Table(name = "waitingSetting")
public class WaitingSetting {       // 웨이팅 설정

    @EmbeddedId
    private Restaurant restaurantNo;        // 식당 고유 번호(fk)

    @Column(name = "waiting_status")
    private String waitingStatus;       // 웨이팅 여부

    @Column(name = "on_off")
    private int onOff;      // 웨이팅 여부

    @Column(name = "start_time")
    private LocalTime startTime;        // 웨이팅 시작 시간

    @Column(name = "end_time")
    private LocalTime endTime;      // 웨이팅 마감 시간
}
