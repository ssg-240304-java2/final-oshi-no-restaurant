package kr.oshino.eataku.review.admin.model.dto;

import kr.oshino.eataku.common.enums.Scope;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.review.user.entity.ReviewTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReviewListDTO {

    private int reviewNo;
    private String reviewName;
    private String reviewContent;
    private Scope scope;
    private String imgUrl;
    private Set<String> reviewTags;
    private String nickName;
    private Long restaurantNo;
    private LocalDateTime reviewDate;
}
