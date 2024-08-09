package kr.oshino.eataku.test.waiting;

import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.waiting.entity.Waiting;
import kr.oshino.eataku.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WaitingTest {

    @Autowired
    public WaitingRepository waitingRepository;

    @Autowired
    public RestaurantRepository restaurantRepository;

    @Autowired
    public MemberRepository memberRepository;

    @Test
    public void contextLoad() {

    }

    @Test
    @DisplayName("웨이팅 등록 테스트")
    void waitingCreateTest() {

        // given

        // when
        Waiting waitingTest = waitingRepository.save(
                Waiting.builder()
                        .partySize(4)
                        .waitingStatus(StatusType.대기중)
                        .build()
        );

        // then
        assertThat(waitingTest).isNotNull();
        assertThat(waitingTest.getPartySize()).isEqualTo(4);
        assertThat(waitingTest.getWaitingNo()).isEqualTo(1);
    }


}
