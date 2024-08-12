package kr.oshino.eataku.test.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import kr.oshino.eataku.common.enums.StatusType;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
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
import java.sql.Time;
import java.util.List;

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

    private Member testMember;
    private RestaurantInfo testRestaurant;

    @BeforeEach
    public void setUp() {
        // Set up test member
        testMember = Member.builder()
                .account("testuser")
                .password("password")
                .name("Test User")
                .nickname("testnick")
                .birthday(Date.valueOf("1990-01-01"))
                .gender("M")
                .email("testuser@example.com")
                .weight("70kg")
                .build();
        memberRepository.save(testMember);

        // Set up test restaurant
        testRestaurant = new RestaurantInfo();
        testRestaurant.setRestaurantName("Test Restaurant");
        testRestaurant.setDescription("A test restaurant.");
        testRestaurant.setRestaurantAddress("123 Test Street");
        testRestaurant.setFoodType("Korean");
        testRestaurant.setOpeningTime(Time.valueOf("09:00:00"));
        testRestaurant.setClosingTime(Time.valueOf("22:00:00"));
        testRestaurant.setContact("010-1234-5678");
        testRestaurant.setPostNumber("12345");
        testRestaurant.setImgUrl("http://example.com/image.png");
        testRestaurant.setXCoordinate(37.5665);
        testRestaurant.setYCoordinate(126.9780);
        restaurantRepository.save(testRestaurant);
    }

    @AfterEach
    public void tearDown() {

        waitingRepository.deleteAll();
        memberRepository.deleteAll();
        restaurantRepository.deleteAll();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("ALTER TABLE tbl_member AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE tbl_restaurant_info AUTO_INCREMENT = 1").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    @DisplayName("웨이팅 등록 테스트")
    void waitingCreateTest() {

        // given

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
        Waiting waitingTest = waitingRepository.save(
                Waiting.builder()
                        .member(testMember)
                        .restaurantInfo(testRestaurant)
                        .partySize(4)
                        .waitingStatus(StatusType.대기중)
                        .build()
        );
        ReadWaitingRequestDto test = new ReadWaitingRequestDto(1L, 1, null);

        // when
        List<ReadWaitingResponseDto> testList = waitingService.getWaitingListByMemberNo(test);

        // then
        System.out.println(testList);
    }


}
