package kr.oshino.eataku.restaurant.admin.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_reservation_setting")
public class ReservationSetting {

    @Id
    @ManyToOne
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantNo;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    @Column(name = "reservation_people")
    private int reservationPeople;
}
