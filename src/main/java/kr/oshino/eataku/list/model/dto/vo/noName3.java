package kr.oshino.eataku.list.model.dto.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "list_member") // 임시
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class noName3 {

    //유저 이름
    private String name;

    //유저 닉네임
    private String nickname;

    //유저 사진
    private String imgUrl;

}
