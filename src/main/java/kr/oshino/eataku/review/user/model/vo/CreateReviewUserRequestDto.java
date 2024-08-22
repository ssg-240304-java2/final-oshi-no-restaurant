package kr.oshino.eataku.review.user.model.vo;
import kr.oshino.eataku.common.enums.Scope;
import lombok.*;

import java.util.Set;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateReviewUserRequestDto {

    private Long memberNo;
    private int reviewNo;
    private Long restaurantNo;
    private String reviewContent;
    private String scope;
    private Set<String> reviewTags;


    public Scope getScopeEnum() {
        return Scope.valueOf(scope);    // Enum으로 변환
    }

}
