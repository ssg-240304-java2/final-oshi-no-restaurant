package kr.oshino.eataku.review.user.controller;
import kr.oshino.eataku.review.user.model.vo.CreateReviewUserRequestDto;
import kr.oshino.eataku.review.user.model.vo.CreateReviewUserResponseDto;
import kr.oshino.eataku.review.user.service.ReviewUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
//@RequestMapping("/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReviewUserController {

    private final ReviewUserService reviewUserService;

    /**
     * 리뷰 페이지 이동
     * @return
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

}
