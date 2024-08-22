package kr.oshino.eataku.reservation.user.model.dto.responseDto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MapDto {
    private Double x_coordinate;
    private Double y_coordinate;
}
