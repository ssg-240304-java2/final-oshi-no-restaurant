package kr.oshino.eataku.list.model.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

 // 임시
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NoName3 {

    //유저 이름
    private String name;

    //유저 닉네임
    private String nickname;

    //유저 사진
    private String imgUrl;

}
