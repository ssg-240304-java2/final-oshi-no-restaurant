package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByToMemberAndCreatedAtAfterOrderByCreatedAtDesc(Long toMember, LocalDateTime createAt);
}
