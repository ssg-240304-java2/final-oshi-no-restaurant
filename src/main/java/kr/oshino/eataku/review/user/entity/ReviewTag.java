package kr.oshino.eataku.review.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

//@Entity
//@Table(name = "tbl_reviewTag")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ReviewTag {

    @ManyToOne
    @JoinColumn(name = "Review")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "TagList")
    private TagList tagList;
}
