package kr.oshino.eataku.test;

import kr.oshino.eataku.member.model.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberTest {

    @Autowired
    public MemberRepository memberRepository;

    @AfterEach
    public void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("테스트")
    void test1() {

        // given

        // when

        // then
    }
}
