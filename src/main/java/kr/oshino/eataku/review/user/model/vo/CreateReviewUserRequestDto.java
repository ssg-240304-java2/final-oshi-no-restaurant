package kr.oshino.eataku.review.user.model.vo;
import kr.oshino.eataku.common.enums.Scope;
import kr.oshino.eataku.review.user.entity.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateReviewUserRequestDto {

    private Long memberNo;
    private int reviewNo;
    private Long restaurantNo;
    private String restaurantName;
    private String reviewContent;
    private Scope scope;
    private Set<String> reviewTags;
    private String imgUrl;
    private LocalDateTime reviewDate;

    public CreateReviewUserRequestDto(Review review) {
        this.reviewNo = review.getReviewNo();
        this.restaurantName = review.getRestaurantInfo().getRestaurantName();
        this.reviewContent = review.getReviewContent();
        this.scope = review.getScope();
        this.reviewTags = review.getReviewTags();
        this.imgUrl = review.getImgUrl();
    }


//    public Scope getScopeEnum() {
//        return Scope.valueOf(scope);    // Enum으로 변환
//    }

}
