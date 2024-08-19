package kr.oshino.eataku.restaurant.admin.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_temporary_save")
public class TemporarySave {

    @Id
    @Column(name = "company_no")
    private Long companyNo;      // 사업자 등록 번호

    @Column(name = "representative_name")
    @NotNull
    @NotBlank(message="비워둘 수 없는 칸입니다.")
    private String representativeName;      // 대표자 명

    @Column(name = "company_name")
    @NotNull
    private String companyName;     // 상호명

    @Column(name = "business_address")
    @NotNull
    private String businessAddress;     // 사업장 주소

    @Column(name = "img_url")
    @NotNull
    private String imgUrl;      // 식당 메인 사진

    @Column(name = "account")
    @NotNull
    private String account;
}
