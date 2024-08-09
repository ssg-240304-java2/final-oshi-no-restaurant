package kr.oshino.eataku.test;

import kr.oshino.eataku.common.enums.AccountAuth;
import kr.oshino.eataku.member.entity.Follow;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.MemberLoginInfo;
import kr.oshino.eataku.member.model.repository.FollowRepository;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberTest {

    @Autowired
    public MemberRepository memberRepository;
    @Autowired
    public FollowRepository followRepository;

    @AfterEach
    public void deleteAll() {
//        followRepository.deleteAll();
//        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("테스트")
    void test1() {

        // given
        Member member = Member.builder()
                .memberNo(1L)
//                .account("account")
                .auth(AccountAuth.general)
                .email("email")
                .birthday(Date.valueOf("2001-01-21"))
                .gender("gender")
                .imgUrl("imgUrl")
                .name("name")
                .nickname("nickname")
                .phone("phone")
//                .password("password")
//                .weight(null)
//                .created_at(null)
                .build();

        MemberLoginInfo memberLoginInfo = MemberLoginInfo.builder()
                .member(member)
                .account("account")
                .password("password")
                .build();

        Member followedMember = Member.builder()
                .memberNo(2L)
                .build();

        // when
        member.setMemberLoginInfo(memberLoginInfo);
        memberRepository.save(member);
        memberRepository.save(followedMember);

        Follow follow = Follow.builder()
                .fromMemberNo(member)
                .toMemberNo(followedMember)
                .build();

        followRepository.save(follow);
        System.out.println("follow = " + follow);

        // then
        Member savedMember = memberRepository.findById(member.getMemberNo()).orElse(null);
        MemberLoginInfo savedMemberLoginInfo = memberRepository.findById(savedMember.getMemberNo()).get().getMemberLoginInfo();

        System.out.println("member1 = " + member);
        System.out.println("member2 = " + savedMember);

        assertThat(savedMember.getName()).isEqualTo(member.getName());
        savedMember.setName("kyungho");
//        assertThat(savedMember.getName()).isEqualTo(member.getName());
        assertThat(savedMemberLoginInfo).isEqualTo(member.getMemberLoginInfo());

        System.out.println("follow = " + follow);

    }
}
