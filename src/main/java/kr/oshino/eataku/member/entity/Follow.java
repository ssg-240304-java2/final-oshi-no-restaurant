package kr.oshino.eataku.member.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.member.model.vo.FollowId;
import kr.oshino.eataku.member.model.vo.FollowMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "a_tbl_follow")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SecondaryTable(
        name = "tbl_member",
        // @PrimaryKeyJoinColumn(referenceColumnName) 생략 가능하다.
        pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id")
)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromMemberId")
    private Member fromMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toMemberId")
    private Member toMemberId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "toMemberNickname", column = @Column(name = "nickname", table = "tbl_member")),
            @AttributeOverride(name = "toMemberImgUrl", column = @Column(name = "img_url", table = "tbl_member"))
    })
    private FollowMemberInfo toMemberInfo;
}
