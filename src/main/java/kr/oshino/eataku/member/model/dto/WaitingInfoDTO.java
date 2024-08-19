package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class WaitingInfoDTO {
    private Long restaurantNo;
    private Long waitingNo;
    private String restaurantImgUrl;
    private String restaurantName;
    private String restaurantDescription;
    private String restaurantAddress;
    private int partySize;
    private int waitingNumber;
}
