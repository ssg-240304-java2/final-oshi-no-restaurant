package kr.oshino.eataku.restaurant.admin.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservationSetting")
public class ReservationSetting {

    @Id
    @ManyToOne
    @JoinColumn(name = "restaurant_no")
    private Restaurant restaurantNo;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    @Column(name = "reservation_people")
    private int reservationPeople;
}
