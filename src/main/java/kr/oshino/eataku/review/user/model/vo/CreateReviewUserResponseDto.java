package kr.oshino.eataku.review.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewUserResponseDto {

    private Integer httpCode;
    private String message;
    private Long memberNo;
}
