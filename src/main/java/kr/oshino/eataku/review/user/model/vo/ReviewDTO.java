package kr.oshino.eataku.review.user.model.vo;
import kr.oshino.eataku.common.enums.Scope;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {

    private Long memberNo;
    private Long reviewNo;
    private Long restaurantNo;
    private String restaurantName;
    private String reviewContent;
    private Scope scope;
    private Set<String> reviewTags;
    private String imgUrl;
    private LocalDateTime reviewDate;
    private String type;
    private Long serviceNo;

}
