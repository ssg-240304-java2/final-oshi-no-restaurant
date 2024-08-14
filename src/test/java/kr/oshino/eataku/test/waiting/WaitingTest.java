package kr.oshino.eataku.test.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.waiting.entity.Waiting;
import kr.oshino.eataku.waiting.model.dto.requestDto.ReadWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import kr.oshino.eataku.waiting.repository.WaitingRepository;
import kr.oshino.eataku.waiting.service.WaitingService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WaitingTest {

    @Autowired
    public WaitingRepository waitingRepository;

    @Autowired
    public RestaurantRepository restaurantRepository;

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private WaitingService waitingService;


    @Test
    @DisplayName("데이터 지우기")
    void deleteAll() {
        waitingRepository.deleteAll();
//        memberRepository.deleteAll();
//        restaurantRepository.deleteAll();
//
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
//        entityManager.createNativeQuery("ALTER TABLE tbl_member AUTO_INCREMENT = 1").executeUpdate();
//        entityManager.createNativeQuery("ALTER TABLE tbl_restaurant_info AUTO_INCREMENT = 1").executeUpdate();
//        entityManager.getTransaction().commit();
//        entityManager.close();
    }







    @Test
    @DisplayName("웨이팅 등록 테스트")
    void waitingCreateTest() {

        // given
        Member testMember = memberRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException(
                "Member not found with id: 1L"
        ));
        RestaurantInfo testRestaurant = restaurantRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException(
                "Restaurant not found with id: 1L"
        ));

        // when
        Waiting waitingTest = waitingRepository.save(
                Waiting.builder()
                        .member(testMember)
                        .restaurantInfo(testRestaurant)
                        .partySize(4)
                        .waitingStatus(StatusType.대기중)
                        .build()
        );

        // then
    }





    @Test
    @DisplayName("웨이팅 조회 테스트")
    void waitingSelectTest() {

        // given
        Member testMember = memberRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException(
                "Member not found with id: 1L"
        ));
        RestaurantInfo testRestaurant = restaurantRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException(
                "Restaurant not found with id: 1L"
        ));

        Waiting waitingTest = waitingRepository.save(
                Waiting.builder()
                        .member(testMember)
                        .restaurantInfo(testRestaurant)
                        .partySize(4)
                        .waitingStatus(StatusType.대기중)
                        .build()
        );
        ReadWaitingRequestDto test = new ReadWaitingRequestDto(1L, 1L, null);

        // when
        List<ReadWaitingResponseDto> testList = waitingService.getWaitingListByMemberNo(test);

        // then
        System.out.println(testList);
    }





    @Test
    @DisplayName("웨이팅 수정 테스트")
    void waitingUpdateTest() {
        // given
        Waiting waiting = waitingRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException(
                "Waiting not found with id: 1L"
        ));

        // when
        waiting.visit();
        waitingRepository.save(waiting);

        // then
        assertThat(waiting.getWaitingStatus()).isEqualTo(StatusType.방문완료);
    }





    @Test
    @DisplayName("웨이팅 삭제 테스트")
    void waitingDeleteTest() {
        // given
        Waiting waiting = waitingRepository.findById(2L).orElseThrow(() -> new EntityNotFoundException(
                "Waiting not found with id: 1L"
        ));

        // when
        waiting.cancel();
        waitingRepository.save(waiting);

        // then
        assertThat(waiting.getWaitingStatus()).isEqualTo(StatusType.대기취소);
    }
}
