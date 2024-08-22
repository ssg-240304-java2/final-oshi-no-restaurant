package kr.oshino.eataku.review.user.controller;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.review.user.entity.Review;
import kr.oshino.eataku.review.user.model.vo.CreateReviewUserRequestDto;
import kr.oshino.eataku.review.user.model.vo.CreateReviewUserResponseDto;
import kr.oshino.eataku.review.user.service.ReviewUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
//@RequestMapping("/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReviewUserController {

    private final ReviewUserService reviewUserService;

    /**
     * 리뷰 페이지 이동
     * @return
     *
     * 로그인 정보
     * CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     *         Long loginedRestaurantNo = member.getRestaurantNo();
     *
     */
    @GetMapping("/reviewPage")
    public String reviewPage() {
        System.out.println("리뷰페이지 등장");
    return "review/review";
    }

    /**
     * 리뷰 등록
     */
    @PostMapping("/reviewPage")
    public ResponseEntity<CreateReviewUserResponseDto> insertReview(@RequestBody CreateReviewUserRequestDto CreateReviewUserRequestDto){
        System.out.println("createReviewUserResponseDto 확인~~~@!@= " + CreateReviewUserRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewUserService.insertReview(CreateReviewUserRequestDto));

    }

    // 내 리뷰 보기
    @GetMapping("/myInfo/review")
    public String myReviewPage(Model model){
        // 로그안 멤버 넘버
        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
              Long loginedMemberNo = member.getMemberNo();


        List<CreateReviewUserRequestDto> myReview =
                reviewUserService.getAllReviewByMember(loginedMemberNo);
        model.addAttribute("myReview", myReview);
        System.out.println("My Review: " + myReview);
        // 리스트에 null이 포함되어 있는지 확인
        for (CreateReviewUserRequestDto review : myReview) {
            System.out.println(review);
        }

        return "review/myReview";
    }

    // 리뷰 삭제
    @PostMapping("/deleteReview")
    @ResponseBody
    public void deleteReview(@RequestParam("reviewNo") Integer reviewNo){
        System.out.println("reviewNo = " + reviewNo);
        reviewUserService.deleteReview(reviewNo);

    }

    // 리뷰 수정
    @PostMapping("/modifyReview")
    @ResponseBody
    public String modifyReview(@RequestParam("reviewNo") int reviewNo,
                               @RequestParam("newReviewContent") String newReviewContent) {
        reviewUserService.modifyReview(reviewNo, newReviewContent);
        return "";
    }



}

