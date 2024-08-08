package kr.oshino.eataku.list.entity;


import jakarta.persistence.*;
import kr.oshino.eataku.list.model.vo.RestaurantList;
import kr.oshino.eataku.member.entity.Member;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_my_list")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SecondaryTables({
        @SecondaryTable(name= "tbl_restaurant_info", pkJoinColumns = @PrimaryKeyJoinColumn(name = "restaurant_no")),
        @SecondaryTable(name= "tbl_member", pkJoinColumns = @PrimaryKeyJoinColumn(name = "member_no")),
        @SecondaryTable(name= "tbl_average_rating", pkJoinColumns = @PrimaryKeyJoinColumn(name = "rating_no"))
})
public class MyList {

    /* 리스트 번호 */
    @Id
    @Column(name = "list_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listNo;

    /* 회원 번호 */
    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    /* 리스트 이름 */
    @Column(name = "list_name")
    private String listName;

    /* 리스트 공개 여부 */
    @Column(name = "list_status")
    private String listStatus;

    /* 리스트 공유 횟수 */
    @Column(name = "list_share")
    private String listShare;

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(
            name = "tbl_mylist_info",
            joinColumns = @JoinColumn(name="list_no", referencedColumnName = "listNo")
    )
    private Set<RestaurantList> myLists;



}
