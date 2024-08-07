package kr.oshino.eataku.member.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_no;

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
