package kr.oshino.eataku.restaurant.admin.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "restaurant")
@Embeddable
public class Restaurant implements Serializable {

    @Id
    @Column(name = "restaurant_no")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String restaurantNo;        // 식당 고유 번호(pk)

    @Column(name = "restaurant_name")
    @NotNull
    private String restaurantName;          // 식당명

    @Column(name = "restaurant_address")
    @NotNull
    private String restaurantAddress;       // 도로명 주소

    @Column(name = "food_type")
    private String foodType;        // 음식 카테고리

    @Column(name = "business_hour")
    private LocalTime businessHour;         // 영업시간

    @Column(name = "contact")
    private String contact;         // 연락처

    @Column(name = "post_number")
    private String postNumber;      // 우편번호

    @Column(name = "img_url")
    private String imgUrl;      // 식당 메인 사진

    @Column(name = "x_coordinate")
    @NotNull
    private Double xCoordinate;     // 위도

    @Column(name = "y_coordinate")
    @NotNull
    private Double yCoordinate;     // 경도

}
