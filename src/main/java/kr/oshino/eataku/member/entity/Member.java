package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "a_tbl_member")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;
    private String password;
    private String name;
    private String nickname;
    private Date birthday;
    private String gender;
    private String email;
    private String weight;

    @Column(name = "img_url")
    private String imgUrl;
    private String phone;
}
