package kr.oshino.eataku.restaurant.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MenuDTO {

    private int menuNo;
    private Long restaurantNo;
    private String menuName;
    private String description;
    private String imgUrl;
}
