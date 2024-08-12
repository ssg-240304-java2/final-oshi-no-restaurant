package kr.oshino.eataku.reservation.user.service;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.reservation.user.model.dto.requestDto.CreateReservationUserRequestDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.CreateReservationUserResponseDto;
import kr.oshino.eataku.reservation.user.repository.ReservationRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class ReservationUserService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public CreateReservationUserResponseDto registerReservation(CreateReservationUserRequestDto createReservationUserRequestDto) {

        // 예약 가능 여부 확인하기


        Member member = memberRepository.findById((long) createReservationUserRequestDto.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberNo: " + createReservationUserRequestDto.getMemberNo()));

        RestaurantInfo restaurantInfo = restaurantRepository.findById((long) createReservationUserRequestDto.getRestaurantNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurantNo: " + createReservationUserRequestDto.getRestaurantNo()));


        Reservation reservation = Reservation.builder()
                .member(member)
                .restaurantInfo(restaurantInfo)
                .partySize(createReservationUserRequestDto.getPartySize())
                .reservationDate(createReservationUserRequestDto.getReservationDate())
                .reservationTime(createReservationUserRequestDto.getReservationTime())
//                .reservationStatus(AccountAuth.ReservationStatus.예약대기) 이거 오류남
                .build();

        reservationRepository.save(reservation);

        // 수정 필요
        return new CreateReservationUserResponseDto(200, "예약 완료", 1);
    }













    // 시간을 가져오는 메소드
    public List<LocalTime> getReservationTimes(Long restaurantNo) {
        List<LocalDateTime> dateTimes = reservationRepository.findTimesByRestaurantNo(restaurantNo);

        // LocalDateTime에서 LocalTime으로 변환
        return dateTimes.stream()
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toList());
    }



    // 최대 인원을 가져오는 메소드
    public int getMaxPeople(Long restaurantNo) {
        // 필요한 경우 데이터베이스에서 최대 인원 정보를 가져옴
        return reservationRepository.findPeopleByRestaurantNo(restaurantNo);
    }





//    /***
//     * 식당 reservation_setting 알아오기
//     * @param restaurantNo
//     * @return
//     */
//
//    public List<Reservation> getReservationsByRestaurantNo(Long restaurantNo) {
//        return reservationRepository.findReservationsByRestaurantNo(restaurantNo);
//    }

    /***
     * 식당 정보 가져오기
     */



}

