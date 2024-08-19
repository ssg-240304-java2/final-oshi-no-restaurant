package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoDTO {

    private Long memberNo;
    private String name;
    private String nickname;
    private String introduction;
    private Date registerDate;
    private String imgUrl;
    private double weight;

    private int followerCnt;
    private int followingCnt;

    private String animalUrl;

    private List<WaitingInfoDTO> waitingInfo;
    private List<ReservationInfoDTO> reservationInfo;

    private String badge;
}
