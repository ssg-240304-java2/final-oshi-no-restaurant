package kr.oshino.eataku.review.user.entity;

import jakarta.persistence.*;
import kr.oshino.eataku.common.enums.Scope;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tbl_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public int getScopeValue(){
        return scope.getValue();
    }

    /* 사진 */
    @Column(name = "img_url")
    private String imgUrl;

    /* 작성 일자 */
    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    /* 예약 타입 */
    @Column
    private String type;

    /* 예약 번호 */
    @Column(name = "reference_number")
    private Long referenceNumber;

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
