package kr.oshino.eataku.member.model.dto;

import kr.oshino.eataku.common.enums.AccountAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long memberNo;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private Date birthday;
    private String imgUrl;
    private String nickname;
    private AccountAuth auth;
    private Date createAt;
    private Date updateAt;

    private String account;
    private String password;

    private String introduction;
}
