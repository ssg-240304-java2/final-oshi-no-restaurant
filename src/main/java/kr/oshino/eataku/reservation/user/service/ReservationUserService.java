package kr.oshino.eataku.reservation.user.service;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.reservation.user.model.dto.requestDto.CreateReservationUserRequestDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.CreateReservationUserResponseDto;
import kr.oshino.eataku.reservation.user.repository.ReservationRepository;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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


    /***
     * 해당 식당의 시간을 가져오는 메소드
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<Map<String, Object>> getAvailableTimeSlots(LocalDate date, Long restaurantNo, int partySize) {
        List<ReservationSetting> reservationSettings = reservationRepository.findAllByDateAndRestaurant(date, restaurantNo);

        return reservationSettings.stream()
                .map(setting -> {
                    Map<String, Object> timeSlot = Map.of(
                            "time", setting.getReservationTime(),
                            "isAvailable", partySize <= setting.getReservationPeople()
                    );
                    return timeSlot;
                })
                .collect(Collectors.toList());
    }




//    /***
//     * 해당 식당의 최대 인원을 가져오는 메소드
//     * @param restaurantNo
//     * @return
//     */
//    public int getMaxPeople(Long restaurantNo) {
//        return reservationRepository.findPeopleByRestaurantNo(restaurantNo);
//    }





    /**
     * 인원수 차감 하는 메소드
     * @param reservationNo
     * @param partySize
     * @param
     */
    @Transactional
    public void subtractPartySize(Long reservationNo, int partySize, LocalTime time) {
        reservationRepository.subtractPartySizeFromReservationPeople(partySize, time, reservationNo);
    }


}

