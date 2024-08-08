package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.Set;


@Entity
@Table(name = "tbl_restaurant_info")
public class RestaurantInfo {

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
    @Enumerated(EnumType.STRING)
    private HashTag hashtag;        // 해시태그

    @OneToMany()
    private Set<Menu> menu;     // 메뉴

    @OneToMany()
    private Set<ReservationSetting> reservationSetting;         // 예약 설정

    @OneToMany()
    private Set<WaitingSetting> waitingSetting;         // 웨이팅 설정

    @OneToOne()
    private Certification certification;        // 사업자 등록 인증

    @OneToOne()
    private AccountInfo accountInfo;        // 계정 정보

}
