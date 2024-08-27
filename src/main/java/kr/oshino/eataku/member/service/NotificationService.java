package kr.oshino.eataku.member.service;

import kr.oshino.eataku.member.entity.Notification;
import kr.oshino.eataku.member.model.dto.NotificationDTO;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.member.model.repository.NotificationRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public void insertNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public String createNotificationMessage(Long referenceNo, String type) {

        log.info("🔔🔔 [ NotificationService ] createNotificationMessage referenceNo: {}, type: {} 🔔🔔", referenceNo, type);

        String msg = "";
        String referenceMember = "";
        String referenceRestaurant = "";

        if (type.equals("follow")) referenceMember = memberRepository.findNameByMemberNo(referenceNo);
        else referenceRestaurant = restaurantRepository.findRestaurantNameByRestaurantNo(referenceNo);

        switch (type) {
            case "follow":
                msg = referenceMember + "님이 당신을 팔로우했습니다.";
                break;
            case "reservation":
                msg = referenceRestaurant + " 식당을 예약했습니다.";
                break;
            case "waiting":
                msg = referenceRestaurant + " 웨이팅을 등록했습니다.";
                break;
            case "review":
                msg = referenceRestaurant + " 이용은 만족스러우셨나요? 리뷰를 남겨주세요.";
                break;
        }

        return msg;
    }

    public List<NotificationDTO> selectNotification(Long memberNo) {

        List<Notification> notifications = notificationRepository.findAllByToMemberAndCreatedAtAfterOrderByCreatedAtDesc(memberNo, LocalDateTime.now().minusDays(14));
        List<NotificationDTO> result = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notiDTO = new NotificationDTO();

            notiDTO.setType(notification.getType());
            if (notification.getType().equals("follow") || notification.getType().equals("list")) {
                notiDTO.setReferenceName(memberRepository.findNameByMemberNo(notification.getReferenceNumber()));
            }
            else if (notification.getType().equals("reservation") || notification.getType().equals("waiting")) {
                notiDTO.setReferenceName(restaurantRepository.findRestaurantNameByRestaurantNo(notification.getReferenceNumber()));
            }
            notiDTO.setDate(notification.getCreatedAt());
            notiDTO.setMessage(notification.getMessage());
            notiDTO.setServiceType(notification.getServiceType());
            notiDTO.setServiceNo(notification.getServiceNo());

            result.add(notiDTO);
        }

        return result;
    }
}
