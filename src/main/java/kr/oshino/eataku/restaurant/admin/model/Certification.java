package kr.oshino.eataku.restaurant.admin.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "certification")
public class Certification {

    @EmbeddedId
    private Restaurant restaurantNo;

    @Column(name = "company_no")
    private int companyNo;

    @Column(name = "representative_name")
    private String representativeName;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "business_address")
    private String businessAddress;
}
