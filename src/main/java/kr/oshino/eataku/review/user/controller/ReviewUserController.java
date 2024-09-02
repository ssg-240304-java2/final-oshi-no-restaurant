package kr.oshino.eataku.review.user.controller;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.review.user.entity.Review;
import kr.oshino.eataku.review.user.model.vo.CreateReviewUserResponseDto;
import kr.oshino.eataku.review.user.model.vo.ReviewDTO;
import kr.oshino.eataku.review.user.repository.ReviewRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping("/")
//@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReviewUserController {

    private final ReviewUserService reviewUserService;
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;


    /**
     * 리뷰 페이지 이동
     *
     * @return
     */
    @GetMapping("/reviewPage")
    public String reviewPage(@RequestParam("restaurantNo") Long restaurantNo,
                             @RequestParam("serviceType") String serviceType,
                             @RequestParam("serviceNo") Long serviceNo,
                             Model model) {

        Review review = reviewUserService.selectReview(restaurantNo, serviceType, serviceNo);

        model.addAttribute("review", review);

        return "review/review";
    }

    /**
     * 리뷰 등록
     */
    @PostMapping("/reviewPage")
    public ResponseEntity<String> insertReview(@RequestPart("file") MultipartFile file,
                                               @RequestPart("jsonData") ReviewDTO review) {
        // 로그인 멤버 넘버
        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedMemberNo = member.getMemberNo();

        log.info("loginedMemberNo: {}", loginedMemberNo);
        log.info("review : {}", review);
        log.info("file : {}", file);

        boolean isSuccess = false;
        isSuccess = reviewUserService.insertReview(loginedMemberNo,review,file);

        return ResponseEntity.ok("/detail/"+review.getRestaurantNo()+"/detailPage#more-button");

    }


    // 내 리뷰 보기
    @GetMapping("/myInfo/review")
    public String myReviewPage(Model model) {
        // 로그안 멤버 넘버
        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedMemberNo = member.getMemberNo();


        List<ReviewDTO> myReview = reviewUserService.getAllReviewByMember(loginedMemberNo);

        model.addAttribute("myReview", myReview);

        return "review/myReview";
    }

    // 리뷰 삭제
    @PostMapping("/deleteReview")
    @ResponseBody
    public void deleteReview(@RequestParam("reviewNo") Integer reviewNo) {
        System.out.println("reviewNo = " + reviewNo);
        reviewUserService.deleteReview(reviewNo);

    }

    // 리뷰 수정(prompt)
//    @PostMapping("/modifyReview")
//    @ResponseBody
//    public String modifyReview(@RequestParam("reviewNo") int reviewNo,
//                               @RequestParam("newReviewContent") String newReviewContent,
//                               @RequestParam("newScope") String newScope,
//                               @RequestParam("newReviewTags") String newReviewTags) {
//
//
//        System.out.println("Received modifyReview request:");
//        System.out.println("reviewNo: " + reviewNo);
//        System.out.println("newReviewContent: " + newReviewContent);
//        System.out.println("newScope: " + newScope);
//        System.out.println("newReviewTags: " + newReviewTags);
//
//    // 문자열을 Set으로 변환
//    Set<String> tagsSet = Arrays.stream(newReviewTags.split(","))
//            .map(String::trim)
//            .collect(Collectors.toSet());
//
//
//        reviewUserService.modifyReview(reviewNo, newReviewContent,newScope,tagsSet);
//        return "";
//    }

//    // 리뷰 수정 2트
//    @GetMapping("/reviewModify")
//    public String reviewModify(@RequestParam("reviewNo") int reviewNo, Model model) {
//        Review review = reviewRepository.findById(reviewNo)
//                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다" + reviewNo));
//
//        CreateReviewUserRequestDto createReviewUserRequestDto = reviewUserService.getReviewById(reviewNo);
//
//        model.addAttribute("review", createReviewUserRequestDto);
////        model.addAttribute("reviewNo", reviewNo);
////        model.addAttribute("restaurantName", review.getRestaurantInfo().getRestaurantName());
////        model.addAttribute("reviewContent", review.getReviewContent());
////        model.addAttribute("scope", review.getScope());
////        model.addAttribute("reviewTags", review.getReviewTags());
////        model.addAttribute("imgUrl", review.getImgUrl());
//
//        System.out.println("리뷰 수정 reviewNo = " + reviewNo);
//
//        return "review/reviewModify";
//    }

//    @PostMapping("/reviewModify")
//    @ResponseBody
//    public String reviewModify(@RequestBody CreateReviewUserRequestDto createReviewUserRequestDto,
//                               @RequestPart(value = "file", required = false) MultipartFile newFile) throws IOException {
//
//
//
//        log.info("==================================================");
//
//        reviewUserService.reviewModify(
//                createReviewUserRequestDto.getReviewNo(),
//                createReviewUserRequestDto.getReviewContent(),
//                createReviewUserRequestDto.getScope(),
//                createReviewUserRequestDto.getReviewTags(),
//                newFile
//        );
//
//        System.out.println("CreateReviewUserRequestDto = " + createReviewUserRequestDto);
//
//
////        reviewUserService.reviewModify(reviewNo, newReviewContent, newScope, newReviewTags, newFile);
//
//
//        return "redirect:/myInfo/review";
//
//    }
}

