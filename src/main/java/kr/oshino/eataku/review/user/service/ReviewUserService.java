package kr.oshino.eataku.review.user.service;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.reservation.user.repository.ReservationRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.review.user.entity.Review;
import kr.oshino.eataku.review.user.model.vo.CreateReviewUserRequestDto;
import kr.oshino.eataku.review.user.model.vo.CreateReviewUserResponseDto;
import kr.oshino.eataku.review.user.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewUserService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;


    /***
     * 리뷰 등록
     * @param createReviewUserRequestDto
     * @return
     */
    @Transactional
    public CreateReviewUserResponseDto insertReview(CreateReviewUserRequestDto createReviewUserRequestDto) {

        Member member = memberRepository.findById(createReviewUserRequestDto.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원 :" +createReviewUserRequestDto.getMemberNo()));

        RestaurantInfo restaurantInfo = restaurantRepository.findById((long) createReviewUserRequestDto.getRestaurantNo())
                .orElseThrow(()->new IllegalArgumentException("없는 식당" +createReviewUserRequestDto.getRestaurantNo()));


        Review review = Review.builder()
                .member(member)
                .restaurantInfo(restaurantInfo)
                .reviewContent(createReviewUserRequestDto.getReviewContent())
                .reviewTags(createReviewUserRequestDto.getReviewTags())
                .scope(createReviewUserRequestDto.getScopeEnum())
                .reviewNo(createReviewUserRequestDto.getReviewNo())
                .build();

        System.out.println("review = " + review);

        reviewRepository.save(review);

        return new CreateReviewUserResponseDto(200,"리뷰 작성 완료", 1L);

    }


}
