package kr.oshino.eataku.review.user.service;
import kr.oshino.eataku.common.enums.Scope;
import kr.oshino.eataku.common.util.FileUploadUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewUserService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    FileUploadUtil fileUploadUtil;


    /***
     * 리뷰 등록
     * @param createReviewUserRequestDto
     * @return
     */
    @Transactional
    public CreateReviewUserResponseDto insertReview(CreateReviewUserRequestDto createReviewUserRequestDto, MultipartFile file) {

        String uploadImgUrl = "";

        // 파일 저장 로직 추가
        if (file != null && !file.isEmpty()) {
            try {
                uploadImgUrl = fileUploadUtil.uploadFile(file);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed. Please try again.", e);
            }
        }

        Member member = memberRepository.findById(createReviewUserRequestDto.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원 :" + createReviewUserRequestDto.getMemberNo()));

        RestaurantInfo restaurantInfo = restaurantRepository.findById((long) createReviewUserRequestDto.getRestaurantNo())
                .orElseThrow(() -> new IllegalArgumentException("없는 식당" + createReviewUserRequestDto.getRestaurantNo()));


        Review review = Review.builder()
                .member(member)
                .restaurantInfo(restaurantInfo)
                .reviewContent(createReviewUserRequestDto.getReviewContent())
                .reviewTags(createReviewUserRequestDto.getReviewTags())
                .scope(createReviewUserRequestDto.getScope())
                .reviewNo(createReviewUserRequestDto.getReviewNo())
                .imgUrl(uploadImgUrl)
                .reviewDate(LocalDateTime.now())
                .build();

        System.out.println("review = " + review);
        System.out.println("uploadImgUrl = " + uploadImgUrl);


        reviewRepository.save(review);

        return new CreateReviewUserResponseDto(200, "리뷰 작성 완료", 1L);

    }


    public List<CreateReviewUserRequestDto> getAllReviewByMember(Long loginedMemberNo) {


        return reviewRepository.findAllByMember_MemberNo(loginedMemberNo)
                .stream()
                .map(review -> CreateReviewUserRequestDto.builder()
                        .memberNo(review.getMember().getMemberNo())
                        .reviewNo(review.getReviewNo())
                        .restaurantName(review.getRestaurantInfo().getRestaurantName())
                        .reviewContent(review.getReviewContent())
                        .restaurantNo(review.getRestaurantInfo().getRestaurantNo())
                        .scope(review.getScope())
                        .reviewTags(review.getReviewTags())
                        .imgUrl(review.getImgUrl())
                        .reviewDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());


    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(int reviewNo) {
        reviewRepository.deleteById(reviewNo);
    }


    // 리뷰 수정(prompt)
//    @Transactional
//    public void modifyReview(int reviewNo, String newReviewContent, String newScope, Set<String> newReviewTags) {
//        Review review = reviewRepository.findById(reviewNo)
//                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + reviewNo));
//
//        review.setReviewContent(newReviewContent);
//        review.setScope(Scope.valueOf(newScope));
//        review.setReviewTags(newReviewTags);
//
//        reviewRepository.save(review);
//    }

    // 리뷰 수정 2트
//    @Transactional
//    public void reviewModify(int reviewNo, String newReviewContent, String newScope, String newReviewTags, MultipartFile newFile) throws IOException {
//
//       // 기존 리뷰 가져오기
//        Review review = reviewRepository .findById(reviewNo)
//                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없네요~" + reviewNo));
//
//        // 리뷰 내용 업데이트
//        if (newReviewContent != null && !newReviewContent.isEmpty()) {
//        review.setReviewContent(newReviewContent);
//
//        }
//        // 리뷰 별점 업데이트
//        if (newScope != null && !newScope.isEmpty()) {
//            review.setScope(Scope.valueOf(newScope));
//        }
//
//        // 리뷰 태그 업데이트
//        if (newReviewTags != null && !newReviewTags.isEmpty()) {
//            Set<String> tagsSet = Arrays.stream(newReviewTags.split(","))
//                    .map(String::trim)
//                    .collect(Collectors.toSet());
//            review.setReviewTags(tagsSet);
//        }
//        // 파일 업데이트
//        if (newFile != null && !newFile.isEmpty()) {
//            String uploadImgUrl = fileUploadUtil.uploadFile(newFile);
//            review.setImgUrl(uploadImgUrl);
//        }

        // 수정된 리뷰 저장
//        reviewRepository.save(review);


    // 리뷰 수정 3트
    @Transactional(readOnly = true)
    public CreateReviewUserRequestDto getReviewById(int reviewNo) {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + reviewNo));

        return new CreateReviewUserRequestDto(review);
    }

    public void reviewModify(int reviewNo, String reviewContent, Scope scope, Set<String> reviewTags, MultipartFile newFile) throws IOException {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다" + reviewNo));

        // 리뷰 수정 내용
        review.setReviewContent(reviewContent);
        review.setScope(scope);
        review.setReviewTags(reviewTags);

        // 파일 업로드
        String uploadImgUrl = "";

        // 파일 저장 로직 추가
        if (newFile != null && !newFile.isEmpty()) {
            try {
                uploadImgUrl = fileUploadUtil.uploadFile(newFile);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed. Please try again.", e);
            }

            reviewRepository.save(review);
        }
    }
}