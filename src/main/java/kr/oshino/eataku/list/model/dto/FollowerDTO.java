package kr.oshino.eataku.list.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FollowerDTO {

    private Long followerNo;
    private String imgUrl;
    private String nickname;
    private Long listCount;
}
