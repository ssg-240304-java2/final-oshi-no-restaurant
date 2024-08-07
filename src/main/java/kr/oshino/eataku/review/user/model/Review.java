package kr.oshino.eataku.review.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Review")
@Data
@Setter(AccessLevel.PRIVATE)
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
    private int scope;

    /* 회원 번호 */
    //@ManyToOne
    //@JoinColumn(name = "member_no")
    //private Member member;

    /* 식당 고유 번호 */
    //@ManyToOne
    //@JoinColumn(name = "restaurant_no")
    //private Restaurant restaurant;

}
