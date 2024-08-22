package kr.oshino.eataku.review.user.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.Scope;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SecondaryTables({
        @SecondaryTable(name= "tbl_restaurant_info", pkJoinColumns = @PrimaryKeyJoinColumn(name = "restaurant_no")),
        @SecondaryTable(name= "tbl_member", pkJoinColumns = @PrimaryKeyJoinColumn(name = "member_no"))
})
public class Review {

    /* 리뷰 번호 */
    @Id
    @Column(name = "review_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewNo;

    /* 라뷰 명 */
    @Column(name = "review_name")
    private String reviewName;

    /* 리뷰 내용 */
    @Column(name = "review_content")
    private String reviewContent;

    /* 별점 */
    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private Scope scope;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="tbl_tags",
            joinColumns = @JoinColumn(name = "review_no", referencedColumnName = "review_no")
    )

    /* 태그 */
    @Column(name="review_tag")
    @Enumerated(EnumType.STRING)
    private Set<String> reviewTags;

    /* 회원 번호 */
    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    /* 식당 번호 */
    @ManyToOne
    @JoinColumn(name = "restaurant_no")
    private RestaurantInfo restaurantInfo;

}
