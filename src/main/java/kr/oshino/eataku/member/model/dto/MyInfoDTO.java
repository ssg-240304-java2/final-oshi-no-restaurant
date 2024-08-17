package kr.oshino.eataku.member.model.dto;

import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.waiting.entity.Waiting;
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

    private Waiting waitingInfo;
    private List<Reservation> reservationInfo;

    private String badge;
}
