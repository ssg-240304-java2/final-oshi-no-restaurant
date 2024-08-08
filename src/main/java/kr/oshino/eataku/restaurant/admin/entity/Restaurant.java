package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import kr.oshino.eataku.restaurant.admin.model.vo.AccountInfo;

import java.time.LocalTime;
import java.util.List;

@Entity
//@SecondaryTables({
//    @SecondaryTable(
//            name="tbl_accountInfo",
//            pkJoinColumns = @PrimaryKeyJoinColumn(name="restaurantNo")
//    )
//})
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @Column(name = "restaurant_no")
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

    @Column(name = "hashtag")
    private HashTag hashtag;        // 해시태그
}
