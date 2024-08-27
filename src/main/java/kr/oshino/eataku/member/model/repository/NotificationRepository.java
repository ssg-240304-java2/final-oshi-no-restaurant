package kr.oshino.eataku.member.model.repository;

import kr.oshino.eataku.member.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
