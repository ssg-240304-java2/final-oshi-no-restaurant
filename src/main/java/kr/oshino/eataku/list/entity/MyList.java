package kr.oshino.eataku.list.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.list.model.vo.RestaurantList;
import kr.oshino.eataku.list.model.vo.UserList;
import kr.oshino.eataku.member.entity.Member;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_my_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "member")
@SecondaryTables({
        @SecondaryTable(name = "tbl_restaurant_info", pkJoinColumns = @PrimaryKeyJoinColumn(name = "restaurant_no"))
})
public class MyList {

    /* 리스트 번호 */
    @Id
    @Column(name = "list_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer listNo;

    /* 리스트 이름 */
    @Column(name = "list_name")
    private String listName;

    /* 리스트 공개 여부 */
    @Column(name = "list_status")
    private String listStatus;

    /* 리스트 공유 횟수 */
    @Column(name = "list_share")
    private Long listShare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", referencedColumnName = "member_no")
    private Member member;

    /* 식당 목록 */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_user_list",
            joinColumns = @JoinColumn(name = "list_no", referencedColumnName = "list_no")
    )
    private List<RestaurantList> restaurantList;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "name", column = @Column(name = "name", table = "tbl_member")),
//            @AttributeOverride(name = "nickname", column = @Column(name = "nickname", table = "tbl_member")),
//            @AttributeOverride(name = "imgUrl", column = @Column(name = "img_url", table = "tbl_member"))
//    })
//    private UserList userList;
}
