package kr.oshino.eataku.review.service;

import jakarta.persistence.EntityNotFoundException;
import kr.oshino.eataku.review.admin.model.dto.ReviewListDTO;
import kr.oshino.eataku.review.admin.model.repository.ReviewAdminRepository;
import kr.oshino.eataku.review.user.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewAdminService {

    private final ReviewAdminRepository reviewAdminRepository;

    @Autowired
    private JavaMailSender emailSender;

    public List<ReviewListDTO> selectReviewList(Long loginedRestaurantNo) {

        List<Review> review = reviewAdminRepository.findByRestaurantInfo_RestaurantNo(loginedRestaurantNo);
        List<ReviewListDTO> reviewList = new ArrayList<>();

        if (review != null) {

            for (int i = 0; i < review.size(); i++) {
                ReviewListDTO reviewDto = new ReviewListDTO();
                reviewDto.setReviewNo(review.get(i).getReviewNo());
                reviewDto.setReviewName(review.get(i).getReviewName());
                reviewDto.setReviewContent(review.get(i).getReviewContent());
                reviewDto.setReviewTags(review.get(i).getReviewTags());
                reviewDto.setImgUrl(review.get(i).getImgUrl());
                reviewDto.setScope(review.get(i).getScope());
                reviewDto.setNickName(review.get(i).getMember().getNickname());
                reviewDto.setReviewDate(review.get(i).getReviewDate());

                reviewList.add(reviewDto);
            }

            log.info("\uD83C\uDF4E service reviewList: {}, loginedRestaurantNo: {}", reviewList, loginedRestaurantNo);

            return reviewList;
        } else {
            throw new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo);
        }
    }

    public void sendReportEmail(Long reviewId, String reason) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("story0856@naver.com");
        message.setSubject("리뷰 신고 - 리뷰 ID: " + reviewId);
        message.setText("리뷰 ID: " + reviewId + "\n신고 사유: " + reason);
        emailSender.send(message);

    }
}
