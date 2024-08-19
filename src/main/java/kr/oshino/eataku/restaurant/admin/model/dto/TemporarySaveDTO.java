package kr.oshino.eataku.restaurant.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TemporarySaveDTO {

    private Long companyNo;
    private String businessAddress;
    private String companyName;
    private String representativeName;
    private String imgUrl;
    private String account;
}
