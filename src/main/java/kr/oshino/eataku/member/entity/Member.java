package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.AccountAuth;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "tbl_member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"memberLoginInfo"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;

    private String name;
    private String nickname;
    private Date birthday;
    private String gender;
    private String email;

    @Column(columnDefinition = "DOUBLE default 3")
    private Double weight;

    @Column
    private String auth;

    @Column(name = "img_url")
    private String imgUrl;
    private String phone;
    private String introduction;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "member")
    private MemberLoginInfo memberLoginInfo;

    public void increaseWeight(Double num) {
        this.weight += num;
    }

    public void decreaseWeight(Double num) {
        this.weight -= num;
    }
}
