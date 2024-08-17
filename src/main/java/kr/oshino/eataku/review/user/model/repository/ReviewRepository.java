package kr.oshino.eataku.review.user.model.repository;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.review.user.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countByMember(Member toMember);
}
