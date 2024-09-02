package kr.oshino.eataku.member.service;

import kr.oshino.eataku.common.enums.ReservationStatus;
import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.common.util.FileUploadUtil;
import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.repository.MyListRepository;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.MemberLoginInfo;
import kr.oshino.eataku.member.model.dto.*;
import kr.oshino.eataku.member.model.repository.FollowRepository;
import kr.oshino.eataku.member.model.repository.MemberLoginInfoRepository;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.reservation.user.repository.ReservationRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.AccountInfoRepository;
import kr.oshino.eataku.review.user.repository.ReviewRepository;
import kr.oshino.eataku.waiting.entity.Waiting;
import kr.oshino.eataku.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    // 메일 전송을 위한 메일서비스
    private final MailService mailService;

    @Autowired
    FileUploadUtil fileUploadUtil;


    public void insertNewMember(MemberDTO newMember) {

        Member member = Member.builder()
                .name(newMember.getName())
                .email(newMember.getEmail())
                .phone(newMember.getPhone())
                .auth("ROLE_GENERAL")
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

        String animalUrl = memberRepository.findAnimalImgUrlByWeight(member.getWeight());
        member.setAnimalUrl(animalUrl);

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

        // (+) 뱃지 조회
        String badge = memberRepository.findBadgeByMemberNo(member.getMemberNo());
        member.setBadge(badge);

        return member;
    }

    public MyInfoDTO selectMyProfile(Long logginedMemberNo) {

        // (+) 프로필 정보 조회
        Member memberInfo = memberRepository.findByMemberNo(logginedMemberNo);
        MyInfoDTO member = new MyInfoDTO();

        if (memberInfo != null) {
            member.setName(memberInfo.getName());
            member.setNickname(memberInfo.getNickname());
            member.setIntroduction(memberInfo.getIntroduction());
            member.setRegisterDate(memberInfo.getCreatedAt());
            member.setImgUrl(memberInfo.getImgUrl());
            member.setWeight(memberInfo.getWeight());
        }

        int followerCnt = followRepository.countByToMemberNo(memberInfo);
        member.setFollowerCnt(followerCnt);
        int followingCnt = followRepository.countByFromMemberNo(memberInfo);
        member.setFollowingCnt(followingCnt);

        String animalUrl = memberRepository.findAnimalImgUrlByWeight(member.getWeight());
        member.setAnimalUrl(animalUrl);
        // (-) 프로필 정보 조회

        // (+) 예약 정보 조회
        List<Reservation> findReservationInfo = reservationRepository.findByMember_MemberNoAndReservationStatusIn(logginedMemberNo, new ReservationStatus[]{ReservationStatus.예약대기, ReservationStatus.예약완료});
        List<ReservationInfoDTO> reservationInfo = new ArrayList<>();

        if (findReservationInfo != null && !findReservationInfo.isEmpty()) {
            for (Reservation reservation : findReservationInfo) {

                ReservationInfoDTO reservationInfoDTO = new ReservationInfoDTO();
                reservationInfoDTO.setRestaurantNo(reservation.getRestaurantInfo().getRestaurantNo());
                reservationInfoDTO.setReservationNo(reservation.getReservationNo());
                reservationInfoDTO.setRestaurantImgUrl(reservation.getRestaurantInfo().getImgUrl());
                reservationInfoDTO.setRestaurantName(reservation.getRestaurantInfo().getRestaurantName());
                reservationInfoDTO.setRestaurantAddress(reservation.getRestaurantInfo().getRestaurantAddress());
                reservationInfoDTO.setReservationDate(Date.valueOf(reservation.getReservationDate()));
                reservationInfoDTO.setReservationTime(Time.valueOf(reservation.getReservationTime()));
                reservationInfo.add(reservationInfoDTO);
            }
        }

        member.setReservationInfo(reservationInfo);
        // (-) 예약 정보 조회

        // (+) 웨이팅 정보 조회
        List<Waiting> findWaitingInfo = waitingRepository.findWaitingByMember_MemberNoAndWaitingStatus(logginedMemberNo, StatusType.대기중);
        List<WaitingInfoDTO> waitingInfo = new ArrayList<>();

        if (findWaitingInfo != null && !findWaitingInfo.isEmpty()) {
            for (Waiting waiting : findWaitingInfo) {
                WaitingInfoDTO waitingInfoDTO = new WaitingInfoDTO();
                waitingInfoDTO.setRestaurantNo(waiting.getRestaurantInfo().getRestaurantNo());
                waitingInfoDTO.setWaitingNo(waiting.getWaitingNo());
                waitingInfoDTO.setRestaurantImgUrl(waiting.getRestaurantInfo().getImgUrl());
                waitingInfoDTO.setRestaurantName(waiting.getRestaurantInfo().getRestaurantName());
                waitingInfoDTO.setRestaurantAddress(waiting.getRestaurantInfo().getRestaurantAddress());
                waitingInfoDTO.setPartySize(waiting.getPartySize());
                waitingInfoDTO.setWaitingNumber(waiting.getSequenceNumber());
                waitingInfo.add(waitingInfoDTO);
            }
        }

        member.setWaitingInfo(waitingInfo);
        // (-) 웨이팅 정보 조회

        // (+) 뱃지 조회
        String badge = memberRepository.findBadgeByMemberNo(logginedMemberNo);
        member.setBadge(badge);
        // (-) 뱃지 조회

        return member;
    }

    public Member selectMyData(Long logginedMemberNo) {
        return memberRepository.findByMemberNo(logginedMemberNo);
    }

    public boolean updateProfile(MultipartFile file, MemberDTO member, Long logginedMemberNo) {

        boolean isSuccess = false;
        String uploadImgUrl = "";
        Member memberInfo = memberRepository.findByMemberNo(logginedMemberNo);

        // 프로필 사진 업로드
        if ( file != null && !file.isEmpty()) {
            try {
                uploadImgUrl = fileUploadUtil.uploadFile(file);
            } catch (IOException e) {
                return false;
            }
        }


        // 바뀐 정보만 바꾸기
        if( memberInfo != null && memberInfo.getMemberNo().equals(logginedMemberNo) ) {

            log.info("👀👀 [ MemberService ] modify to member : {} 👀👀", memberInfo);
            log.info("👀👀 [ MemberService ] modify from member : {} 👀👀", member);
            // 기본 회원정보
            if( !uploadImgUrl.isEmpty() ) {memberInfo.setImgUrl(uploadImgUrl);}
            if( !memberInfo.getName().equals(member.getName()) ) {memberInfo.setName(member.getName());}
            if( !memberInfo.getNickname().equals(member.getNickname()) ) {memberInfo.setNickname(member.getNickname());}
            if( !Objects.equals(memberInfo.getBirthday(),member.getBirthday()) ) {memberInfo.setBirthday(member.getBirthday());}
            if( !Objects.equals(memberInfo.getGender(),member.getGender()) ) {memberInfo.setGender(member.getGender());}
            if( !Objects.equals(memberInfo.getPhone(),member.getPhone()) ) {memberInfo.setPhone(member.getPhone());}
            if( !Objects.equals(memberInfo.getIntroduction(),member.getIntroduction()) ) {memberInfo.setIntroduction(member.getIntroduction());}

            // 계정정보
            MemberLoginInfo tempLoginInfo = memberInfo.getMemberLoginInfo();
            if( !bCryptPasswordEncoder.matches(member.getPassword(), tempLoginInfo.getPassword()) && !(member.getPassword().isEmpty())) {tempLoginInfo.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));}
            memberInfo.setMemberLoginInfo(tempLoginInfo);

            memberRepository.save(memberInfo);
            isSuccess = true;
        }

        return isSuccess;
    }

    public boolean findAccountByNameAndEmail(String name, String email) {

        Member member = memberRepository.findByNameAndEmail(name, email);

        if( member != null && member.getMemberLoginInfo() != null && member.getMemberLoginInfo().getAccount() != null) {

            return mailService.sendEmailFindId(email, member.getMemberLoginInfo().getAccount() );
        }

        return false;
    }

    public boolean findPasswordByIdAndNameAndEmail(String id, String name, String email) {

        Member member = memberRepository.findByMemberLoginInfoAccountAndNameAndEmail(id,name,email);

        if( member != null && member.getMemberLoginInfo() != null) {

            String tempPassword = generateRandomMixAll(8);

            MemberLoginInfo changePasswordInfo = member.getMemberLoginInfo();
            changePasswordInfo.setPassword(bCryptPasswordEncoder.encode(tempPassword));
            member.setMemberLoginInfo(changePasswordInfo);

            memberRepository.save(member);

            return mailService.sendEmailFindPw(email, tempPassword );
        }

        return false;
    }

    public String generateRandomMixAll(int length) {
        SecureRandom secureRandom = new SecureRandom();
        /*
         * 1. 특수문자의 범위 33 ~ 47, 58 ~ 64, 91 ~ 96
         * 2. 숫자의 범위 : 48 ~ 57
         * 3. 대문자의 범위: 65 ~ 90
         * 4. 소문자의 범위: 97 ~ 122
         */
        String charNSpecialChar =
                IntStream.concat(
                                IntStream.concat(
                                        IntStream.rangeClosed(48, 57),
                                        IntStream.rangeClosed(65,90)),
                                IntStream.concat(
                                        IntStream.rangeClosed(97, 122),
                                        "!@#$%^&*=+".chars()))
                        .mapToObj(i -> String.valueOf((char) i))
                        .collect(Collectors.joining());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(charNSpecialChar.charAt(secureRandom.nextInt(charNSpecialChar.length())));
        }
        return builder.toString();
    }

    public boolean selectSignUpByIdAndNameAndEmail(String id, String name, String email) {
        return memberRepository.existsByMemberLoginInfoAccountAndNameAndEmail(id,name,email);
    }

    public List<HistoryDTO> selectHistory(Long logginedMemberNo) {
        List<Object[]> results = memberRepository.selectHistory(logginedMemberNo);
        List<HistoryDTO> historyList = new ArrayList<>();

        for (Object[] result : results) {
            HistoryDTO dto = new HistoryDTO(
                    (String) result[0],  // restaurantName
                    ((Number) result[1]).longValue(),  // restaurantNo
                    (String) result[2],  // imgUrl
                    (String) result[3],  // restaurantAddress
                    (Timestamp) result[4],  // updateAt
                    (String) result[5],  // serviceType
                    ((Number) result[6]).longValue(),  // serviceNo
                    (String) result[7]   // status
            );
            historyList.add(dto);
        }

        return historyList;
    }
}
