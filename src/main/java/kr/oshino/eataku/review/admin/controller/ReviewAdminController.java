package kr.oshino.eataku.review.admin.controller;

import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.review.admin.model.dto.ReviewListDTO;
import kr.oshino.eataku.review.service.ReviewAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.ReverbType;
import java.util.List;

import static java.lang.Integer.parseInt;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class ReviewAdminController {

    private final ReviewAdminService reviewAdminService;

    /***
     * 리뷰 조회
     * @param model
     * @return
     */
    @GetMapping("/review")
    public String selectReview(Model model){

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        List<ReviewListDTO> reviews = reviewAdminService.selectReviewList(loginedRestaurantNo);

        // 별점 통계
        double averageRating = reviews.stream()
                .mapToInt(review -> review.getScope().getValue())
                .average()
                .orElse(0.0);

        String formattedAverageRating = String.format("%.2f", averageRating);

        long[] ratingCounts = new long[5];
        for(ReviewListDTO review: reviews) {
            int ratingIndex = review.getScope().getValue() - 1;
            if(ratingIndex >= 0 && ratingIndex < 5){
                ratingCounts[ratingIndex]++;
            }
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("formattedAverageRating", formattedAverageRating);
        model.addAttribute("ratingCounts", ratingCounts);

        log.info("\uD83C\uDF4E controller review: {}, formattedAverageRating: {}, ratingCounts: {}", reviews, formattedAverageRating, ratingCounts);

        return "restaurant/reviewList";
    }

    /***
     * 리뷰 신고
     * @param reviewId
     * @param reason
     * @return
     */
    @PostMapping("/report")
    public ResponseEntity<String> reviewReport(@RequestParam("reviewId") Long reviewId,
                                               @RequestParam("reason") String reason){

        reviewAdminService.sendReportEmail(reviewId, reason);
        return ResponseEntity.ok("신고가 접수되었습니다.");

    }

}
