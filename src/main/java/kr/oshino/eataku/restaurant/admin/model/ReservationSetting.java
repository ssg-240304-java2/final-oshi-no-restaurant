package kr.oshino.eataku.restaurant.admin.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservationSetting")
public class ReservationSetting {

    @EmbeddedId
    private Restaurant restaurantNo;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    @Column(name = "reservation_people")
    private int reservationPeople;
}
