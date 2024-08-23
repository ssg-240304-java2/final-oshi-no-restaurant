package kr.oshino.eataku.reservation.user.model.dto.responseDto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestaurantInfoDetails {
    private String restaurantName;
    private String restaurantAddress;
    private String imgUrl;
    private Long restaurantNo;
}
