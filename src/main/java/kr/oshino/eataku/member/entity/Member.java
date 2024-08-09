package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.AccountAuth;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "tbl_member")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_no;
    private String account;
    private String password;
    private String name;
    private String nickname;
    private Date birthday;
    private String gender;
    private String email;
    private String weight;

    @Enumerated(EnumType.STRING)
    private AccountAuth auth;

    @Column(name = "img_url")
    private String imgUrl;
    private String phone;

}
