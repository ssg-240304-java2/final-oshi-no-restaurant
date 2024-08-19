package kr.oshino.eataku.reservation.user.service;
import kr.oshino.eataku.common.enums.ReservationStatus;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.reservation.user.model.dto.requestDto.CreateReservationUserRequestDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.CreateReservationUserResponseDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.ReadReservationResponseDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.RestaurantInfoDetails;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.modalDto;
import kr.oshino.eataku.reservation.user.repository.ReservationRepository;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                .reservationStatus(ReservationStatus.예약완료)
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


    /**
     * 인원수 차감 하는 메소드
     *
     * @param reservationNo
     * @param partySize
     * @param
     */
    @Transactional
    public void subtractPartySize(Long reservationNo, int partySize, LocalTime time) {
        reservationRepository.subtractPartySizeFromReservationPeople(partySize, time, reservationNo);
    }

    /***
     * 상세정보가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public modalDto getModalDetails(Long restaurantNo) {
        Optional<Reservation> reservations = reservationRepository.findTopReservationByRestaurantNo(restaurantNo);

        if (reservations.isEmpty()) {
            throw new RuntimeException("예약정보가 없습니다. restaurantNo: " + restaurantNo);
        }

        Reservation reservation = reservations.get();
        System.out.println("reservation = " + reservation);

        return new modalDto(
                reservation.getRestaurantInfo().getRestaurantName(),
                reservation.getPartySize(),
                reservation.getReservationDate(),
                reservation.getReservationTime()
        );

    }

    /***
     * 날짜 가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional
    public List<LocalDate> getAvailableDates(Long restaurantNo) {
        return reservationRepository.findAvailableDatesByRestaurantNo(restaurantNo);
    }


    /**
     * 예약 조회 (회원)
     *
     * @param readReservationResponseDto
     * @return
     */
    @Transactional
    public List<ReadReservationResponseDto> getReservationListByMemberNo(ReadReservationResponseDto readReservationResponseDto) {

        List<ReadReservationResponseDto> reservationList = reservationRepository.findReservationByMemberNo(readReservationResponseDto.getMemberNo());
        System.out.println("reservationList = " + reservationList);

        if (readReservationResponseDto.getReservationStatus() != null) {
            reservationList = reservationList.stream()
                    .filter(dto -> dto.getReservationStatus().equals(readReservationResponseDto.getReservationStatus()))
                    .collect(Collectors.toList());
        }

        return reservationList;
    }


    /***
     * 예약 취소
     *
     */
    @Transactional
    public boolean cancelReservation(int reservationNo) {
        return reservationRepository.findById(reservationNo).map(reservation -> {
            LocalDate reservationDate = reservation.getReservationDate(); // 예약 날짜 가져오기
            LocalTime reservationTime = reservation.getReservationTime(); // 예약 시간 가져오기

            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            // 현재 날짜와 예약 날짜의 차이를 계산
            long daysUntilReservation = ChronoUnit.DAYS.between(currentDate, reservationDate);

            if (daysUntilReservation > 0 || (daysUntilReservation == 0 && currentTime.isBefore(reservationTime.minusHours(24)))) {
                // 예약 취소 처리
                reservationRepository.delete(reservation);

                // 취소한 인원수를 reservation_setting 테이블에 다시 추가
                updateAvailableSeats(reservation.getReservationNo(), reservation.getPartySize());

                return true;
            } else {
                // 취소 불가
                return false;
            }
        }).orElse(false); // 해당 예약이 없는 경우
    }


    /**
     * 취소한 인원 만큼 증가하기
     *
     * @param reservationNo
     * @param partySize
     */
    @Transactional
    public void updateAvailableSeats(int reservationNo, int partySize) {

        reservationRepository.updateAvailableSeats(reservationNo, partySize);
    }


    /***
     * 식당 상세정보가져오기
     * @param restaurantNo
     * @return
     */
    @Transactional

    public RestaurantInfoDetails getRestaurantDetailsByReservation(Long restaurantNo) {
        return reservationRepository.findRestaurantDetailsByReservationNo(restaurantNo)
                .orElseThrow(() -> new IllegalArgumentException("없는 식당 " + restaurantNo));
    }
}
