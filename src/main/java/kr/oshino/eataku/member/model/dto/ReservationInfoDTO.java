package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReservationInfoDTO {
    private Long restaurantNo;
    private int reservationNo;
    private String restaurantImgUrl;
    private String restaurantName;
    private String restaurantDescription;
    private String restaurantAddress;
    private Date reservationDate;
    private Time reservationTime;
}
