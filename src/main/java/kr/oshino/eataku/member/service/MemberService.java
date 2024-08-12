package kr.oshino.eataku.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.oshino.eataku.common.enums.AccountAuth;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.MemberLoginInfo;
import kr.oshino.eataku.member.model.dto.MemberDTO;
import kr.oshino.eataku.member.model.repository.MemberLoginInfoRepository;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberLoginInfoRepository memberLoginInfoRepository;

    public void insertNewMember(MemberDTO newMember) {

        Member member = Member.builder()
                .name(newMember.getName())
                .email(newMember.getEmail())
                .phone(newMember.getPhone())
                .auth(AccountAuth.general)
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

        return memberLoginInfoRepository.existsByAccount(account);
    }

    public boolean checkDuplicateNickname(String nickname) {

        return memberRepository.existsByNickname(nickname);
    }

}
