package kr.oshino.eataku.member.service;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.repository.MyListRepository;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.MemberLoginInfo;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.dto.MemberDTO;
import kr.oshino.eataku.member.model.dto.MemberProfileDTO;
import kr.oshino.eataku.member.model.dto.ZzupListDTO;
import kr.oshino.eataku.member.model.repository.FollowRepository;
import kr.oshino.eataku.member.model.repository.MemberLoginInfoRepository;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.AccountInfoRepository;
import kr.oshino.eataku.review.user.model.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberLoginInfoRepository memberLoginInfoRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final FollowRepository followRepository;
    private final ReviewRepository reviewRepository;
    private final MyListRepository myListRepository;

    public void insertNewMember(MemberDTO newMember) {

        Member member = Member.builder()
                .name(newMember.getName())
                .email(newMember.getEmail())
                .phone(newMember.getPhone())
                .auth("general")
                .weight(3.0)
                .birthday(newMember.getBirthday())
                .nickname(newMember.getNickname())
                .gender(newMember.getGender())
                .build();

        MemberLoginInfo memberLoginInfo = MemberLoginInfo.builder()
                .member(member)
                .account(newMember.getAccount())
                .password(bCryptPasswordEncoder.encode(newMember.getPassword()))
                .build();
        member.setMemberLoginInfo(memberLoginInfo);

        memberRepository.save(member);
    }

    public boolean checkDuplicateAccount(String account) {

        boolean isExist = memberLoginInfoRepository.existsByAccount(account);
        log.info("👀👀 [ MemberService ] exist memberAccount: {} 👀👀", isExist);
        if (isExist){
            return true;
        }

        isExist = accountInfoRepository.existsById(account);
        log.info("👀👀 [ MemberService ] exist restaurantAccount: {} 👀👀", isExist);

        if (isExist){
            return true;
        }

        return false;
    }

    public boolean checkDuplicateNickname(String nickname) {

        return memberRepository.existsByNickname(nickname);
    }

    public MemberProfileDTO selectProfile(Long memberNo) {

        MemberProfileDTO member = new MemberProfileDTO();
        member.setMemberNo(memberNo);

        CustomMemberDetails loginedMember = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedMemberNo = loginedMember.getMemberNo();

        // 회원 기본정보 조회
        Member memberInfo = memberRepository.findById(memberNo).orElse(null);

        if (memberInfo != null) {
            member.setName(memberInfo.getName());
            member.setNickname(memberInfo.getNickname());
            member.setIntroduction(memberInfo.getIntroduction());
            member.setRegisterDate(memberInfo.getCreatedAt());
            member.setImgUrl(memberInfo.getImgUrl());
            member.setWeight(memberInfo.getWeight());
        }

        // 팔로우 여부 조회
        Member fromMember = memberRepository.findById(loginedMemberNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + loginedMemberNo));
        Member toMember = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberNo));

        boolean followed = followRepository.existsByFromMemberNoAndToMemberNo(fromMember, toMember);
        member.setFollowed(followed);

        int followerCnt = followRepository.countByToMemberNo(toMember);
        member.setFollowerCnt(followerCnt);
        int followingCnt = followRepository.countByFromMemberNo(toMember);
        member.setFollowingCnt(followingCnt);

        int reviewCnt = reviewRepository.countByMember(toMember);
        member.setReviewCnt(reviewCnt);

        int publicListCnt = myListRepository.countByMemberAndListStatus(toMember,"Public");
        member.setPublicListCnt(publicListCnt);
        int privateListCnt = myListRepository.countByMemberAndListStatus(toMember,"Private");
        member.setPrivateListCnt(privateListCnt);

        // TODO
        member.setAnimalUrl("파이리");

        List<MyList> publicList = myListRepository.findByMemberAndListStatus(toMember, "Public");
        List<MyList> privateList = myListRepository.findByMemberAndListStatus(toMember, "Private");

        if (publicList != null && !publicList.isEmpty()) {
            member.setPublicList(publicList.stream()
                                         .map(entity -> new ZzupListDTO(entity.getListNo()
                                                 ,entity.getListName()
                                                 ,entity.getListShare()
                                                 ,entity.getMember().getMemberNo()
                                                 ,entity.getMember().getName()
                                                 ,entity.getMember().getImgUrl()))
                                         .collect(Collectors.toList()));
        }

        if (privateList != null && !privateList.isEmpty()) {
            member.setPrivateList(privateList.stream()
                                          .map(entity -> new ZzupListDTO(entity.getListNo()
                                                  ,entity.getListName()
                                                  ,entity.getListShare()
                                                  ,entity.getMember().getMemberNo()
                                                  ,entity.getMember().getName()
                                                  ,entity.getMember().getImgUrl()))
                                          .collect(Collectors.toList()));
        }

        return member;
    }
}
