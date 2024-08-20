package kr.oshino.eataku.restaurant.admin.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_certification")
@Builder
@AllArgsConstructor

public class Certification {


    @OneToOne()
    @JsonBackReference
    @JoinColumn(name = "restaurant_no", referencedColumnName = "restaurant_no")
    private RestaurantInfo restaurantNo;        // 식당 고유 번호(pk)

    @Id
    @Column(name = "company_no")
    private Long companyNo;      // 사업자 등록 번호

    @Column(name = "representative_name")
    @NotNull
    private String representativeName;      // 대표자 명

    @Column(name = "company_name")
    @NotNull
    private String companyName;     // 상호명

    @Column(name = "business_address")
    @NotNull
    private String businessAddress;     // 사업장 주소

    @Column(name = "img_url")
    private String imgUrl;      // 사업자등록증 url
}
