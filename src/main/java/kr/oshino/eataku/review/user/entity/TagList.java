package kr.oshino.eataku.review.user.entity;

import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Table(name = "tbl_review")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class TagList {

    /* 태그 번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_no")
    private int tagNo;

    /* 태그 이름 */
    @Column(name = "tag_name")
    private String tagName;

}
