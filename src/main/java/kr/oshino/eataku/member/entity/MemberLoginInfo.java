package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "a_tbl_member_login_info")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class MemberLoginInfo {

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_no")
    private Member member;

    private String id;
    private String password;
}
