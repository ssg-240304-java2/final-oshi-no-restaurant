package kr.oshino.eataku.list.model.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserList {

    //유저 이름
    private String name;

    //유저 닉네임
    private String nickname;

    //유저 사진
    private String imgUrl;

}
