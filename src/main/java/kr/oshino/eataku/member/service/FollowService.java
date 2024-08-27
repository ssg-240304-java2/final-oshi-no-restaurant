package kr.oshino.eataku.member.service;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.repository.MyListRepository;
import kr.oshino.eataku.member.entity.Follow;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.Notification;
import kr.oshino.eataku.member.model.repository.FollowRepository;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final MyListRepository listRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public List<MyList> findByListNameContainingAndListStatusOrderByListShareDesc(String query, String status) {
        return listRepository.findByListNameContainingAndListStatusOrderByListShareDesc(query, status);
    }

    @Transactional
    public Boolean toggleFollowStatus(Long loginedMemberNo, Long memberNo) {

        Member fromToMember = new Member();
        fromToMember.setMemberNo(loginedMemberNo);

        Member toToMember = new Member();
        toToMember.setMemberNo(memberNo);

        boolean followed = followRepository.existsByFromMemberNoAndToMemberNo(fromToMember,toToMember);

        if(followed) {
            followRepository.deleteByFromMemberNoAndToMemberNo(fromToMember, toToMember);
        } else {
            followRepository.save(Follow.builder()
                                          .fromMemberNo(fromToMember)
                                          .toMemberNo(toToMember)
                                          .build());

            // 알림등록
            Notification notification = Notification.builder()
                    .toMember(toToMember.getMemberNo())
                    .type("follow")
                    .referenceNumber(fromToMember.getMemberNo())
                    .message(notificationService.createNotificationMessage(fromToMember.getMemberNo(), "follow"))
                    .build();

            notificationService.insertNotification(notification);
        }

        return !followed;
    }
}
