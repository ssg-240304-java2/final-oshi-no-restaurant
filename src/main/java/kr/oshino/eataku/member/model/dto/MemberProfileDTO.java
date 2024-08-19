package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDTO {

    private Long memberNo;
    private String name;
    private String nickname;
    private String introduction;
    private Date registerDate;
    private String imgUrl;
    private double weight;

    private Boolean followed;

    private int followerCnt;
    private int followingCnt;

    private int reviewCnt;
    private int publicListCnt;
    private int privateListCnt;

    private String animalUrl;

    private List<ZzupListDTO> publicList;
    private List<ZzupListDTO> privateList;

    private String badge;
}
