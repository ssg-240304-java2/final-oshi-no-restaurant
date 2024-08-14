package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import lombok.*;

import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "tbl_restaurant_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RestaurantInfo {

    @Id
    @Column(name = "restaurant_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantNo;        // 식당 고유 번호(pk)

    @Column(name = "restaurant_name")
    @NotNull
    private String restaurantName;          // 식당명

    @Column(name = "description")
    private String description;         // 식당 소개

    @Column(name = "restaurant_address")
    @NotNull
    private String restaurantAddress;       // 도로명 주소

    @Column(name = "food_type")
    @Enumerated(EnumType.STRING)
    private FoodType foodType;        // 음식 카테고리        // TODO

    @Column(name = "opening_time")
    private Time openingTime;         // 오픈시간

    @Column(name = "closing_time")
    private Time closingTime;       // 마감 시간

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

    @Column(name = "hash_tag")
    @Enumerated(EnumType.STRING)
    private HashTag hashtag;        // 해시태그     // TODO

    @OneToOne(mappedBy = "restaurantNo", cascade = CascadeType.ALL)
    private Certification certification;        // 사업자 등록 인증

    @OneToOne(mappedBy = "restaurantNo", cascade = CascadeType.ALL)
    private AccountInfo accountInfo;        // 계정 정보

    public RestaurantInfo(Long restaurantNo, String restaurantName, String description, String restaurantAddress, FoodType foodType, Time openingTime, Time closingTime, String contact, String postNumber, String imgUrl, Double xCoordinate, Double yCoordinate, HashTag hashtag) {
        this.restaurantNo = restaurantNo;
        this.restaurantName = restaurantName;
        this.description = description;
        this.restaurantAddress = restaurantAddress;
        this.foodType = foodType;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.contact = contact;
        this.postNumber = postNumber;
        this.imgUrl = imgUrl;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.hashtag = hashtag;
    }
}
