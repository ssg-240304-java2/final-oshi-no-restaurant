package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.HashTag;
import lombok.*;

import java.sql.Time;

@Entity
@Table(name = "tbl_restaurant_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class RestaurantInfo {

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getFoodType() {
        return foodType;
    }

    public Time getBusinessHour() {
        return businessHour;
    }

    public String getContact() {
        return contact;
    }

    public String getPostNumber() {
        return postNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Double getxCoordinate() {
        return xCoordinate;
    }

    public Double getyCoordinate() {
        return yCoordinate;
    }

    public HashTag getHashtag() {
        return hashtag;
    }

    public Certification getCertification() {
        return certification;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    @Id
    @Column(name = "restaurant_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantNo;        // 식당 고유 번호(pk)

    @Column(name = "restaurant_name")
    @NotNull
    private String restaurantName;          // 식당명

    @Column(name = "restaurant_address")
    @NotNull
    private String restaurantAddress;       // 도로명 주소

    @Column(name = "food_type")
    private String foodType;        // 음식 카테고리

    @Column(name = "business_hour")
    private Time businessHour;         // 영업시간

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
    @Enumerated(EnumType.STRING)
    private HashTag hashtag;        // 해시태그


    public Long getRestaurantNo() {
        return restaurantNo;
    }


    public RestaurantInfo(Long restaurantNo, String restaurantName, String restaurantAddress, String foodType, Time businessHour, String contact, String postNumber, String imgUrl, Double xCoordinate, Double yCoordinate, HashTag hashtag) {
        this.restaurantNo = restaurantNo;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.foodType = foodType;
        this.businessHour = businessHour;
        this.contact = contact;
        this.postNumber = postNumber;
        this.imgUrl = imgUrl;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.hashtag = hashtag;
    }

    @OneToOne()
    @JoinColumn(name = "restaurant_no", referencedColumnName = "restaurant_no")
    private Certification certification;        // 사업자 등록 인증

    @OneToOne()
    @JoinColumn(name = "restaurant_no", referencedColumnName = "restaurant_no")
    private AccountInfo accountInfo;        // 계정 정보

}
