package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_certification")
public class Certification {

    @Id
    @OneToOne()
    @JoinColumn(name = "restaurant_no")
    private Restaurant restaurantNo;        // 식당 고유 번호(pk)

    @Column(name = "company_no")
    private int companyNo;      // 사업자 등록 번호

    @Column(name = "representative_name")
    @NotNull
    private String representativeName;      // 대표자 명

    @Column(name = "company_name")
    @NotNull
    private String companyName;     // 상호명

    @Column(name = "business_address")
    @NotNull
    private String businessAddress;     // 사업장 주소
}
