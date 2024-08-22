package kr.oshino.eataku.review.user.model.vo;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagDto {
    private int reviewNo;
    private String reviewTag;
}
