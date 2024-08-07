package kr.oshino.eataku.review.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Review")
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
