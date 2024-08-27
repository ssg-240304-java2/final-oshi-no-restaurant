package kr.oshino.eataku.member.controller;

import kr.oshino.eataku.member.entity.Notification;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.dto.NotificationDTO;
import kr.oshino.eataku.member.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notification")
    @ResponseBody
    public List<NotificationDTO> getNotifications() {
        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberNo = member.getMemberNo();

        List<NotificationDTO> notificationList = notificationService.selectNotification(memberNo);

        return notificationList;
    }
}
