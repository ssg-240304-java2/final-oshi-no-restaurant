package kr.oshino.eataku.restaurant.admin.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import kr.oshino.eataku.restaurant.admin.service.RestaurantAdminService;
import kr.oshino.eataku.review.admin.entity.AverageRating;
import kr.oshino.eataku.review.user.entity.Review;
import lombok.*;

import java.sql.Time;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_restaurant_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = {"certification","accountInfo"})
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
    private String restaurantAddress;       // 식당 전체 주소

    @Column(name = "post_code")
    private String postCode;        // 우편 번호

    @Column(name = "address")
    private String address;         // 도로명 주소

    @Column(name = "detail_address")
    private String detailAddress;       // 상세 주소

    @Column(name = "extra_address")
    private String extraAddress;        // 추가 주소

    @Column(name = "opening_time")
    private Time openingTime;         // 오픈시간

    @Column(name = "closing_time")
    private Time closingTime;       // 마감 시간

    @Column(name = "contact")
    private String contact;         // 연락처

    @Column(name = "img_url")
    private String imgUrl;      // 식당 메인 사진

    @Column(name = "x_coordinate")
    private Double xCoordinate;     // 위도

    @Column(name = "y_coordinate")
    private Double yCoordinate;     // 경도

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "tbl_food_type",
        joinColumns = @JoinColumn(name = "restaurant_no", referencedColumnName = "restaurant_no")
    )
    @Column(name = "food_type")
    @Enumerated(EnumType.STRING)
    private Set<FoodType> foodTypes;      // 음식종류

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_hash_tag",
            joinColumns = @JoinColumn(name = "restaurant_no", referencedColumnName = "restaurant_no")
    )
    @Column(name = "hash_tag")
    @Enumerated(EnumType.STRING)
    private Set<HashTag> hashTags;        // 해시태그

    @OneToOne(mappedBy = "restaurantNo", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Certification certification;        // 사업자 등록 인증

    @OneToOne(mappedBy = "restaurantNo", cascade = CascadeType.ALL)
    private AccountInfo accountInfo;        // 계정 정보

    public RestaurantInfo(Long restaurantNo, String restaurantName, String description, String restaurantAddress, String postCode, String address, String detailAddress, String extraAddress, Time openingTime, Time closingTime, String contact, String imgUrl, Double xCoordinate, Double yCoordinate, Set<FoodType> foodTypes, Set<HashTag> hashTags) {
        this.restaurantNo = restaurantNo;
        this.restaurantName = restaurantName;
        this.description = description;
        this.restaurantAddress = restaurantAddress;
        this.postCode = postCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.contact = contact;
        this.imgUrl = imgUrl;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.foodTypes = foodTypes;
        this.hashTags = hashTags;
    }


}

