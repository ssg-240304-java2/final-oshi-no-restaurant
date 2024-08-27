package kr.oshino.eataku.review.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AverageRatingDTO {

    private double averageRating;
    private long[] ratingCounts;
    private Long restaurantNo;
}
