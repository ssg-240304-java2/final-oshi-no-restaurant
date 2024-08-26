package kr.oshino.eataku.reservation.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationCountDTO {

    private String date;
    private Long count;
}
