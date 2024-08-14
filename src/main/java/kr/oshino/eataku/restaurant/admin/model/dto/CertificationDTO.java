package kr.oshino.eataku.restaurant.admin.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CertificationDTO {

    private Long companyNo;
    private Long restaurantNo;
    private String businessAddress;
    private String companyName;
    private String representativeName;
    private String imgUrl;
}
