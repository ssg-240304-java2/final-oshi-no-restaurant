package kr.oshino.eataku.restaurant.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_reservation_setting")
@Builder
@AllArgsConstructor
@ToString(exclude = {"restaurantNo"})
public class ReservationSetting {

    @Id
    @Column(name = "reservation_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationNo;

    @ManyToOne()
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantNo;

    @Column(name = "reservation_date")
    private Date reservationDate;

    @Column(name = "reservation_time")
    private Time reservationTime;

    @Column(name = "reservation_people")
    private int reservationPeople;

    public void subtractPeople(int num) {
        this.reservationPeople -= num;
    }
}
