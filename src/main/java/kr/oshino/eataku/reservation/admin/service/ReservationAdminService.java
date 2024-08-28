package kr.oshino.eataku.reservation.admin.service;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationDTO;
import kr.oshino.eataku.reservation.admin.repository.ReservationAdminRepository;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationAdminService {

    private final ReservationAdminRepository reservationAdminRepository;
    private final MemberRepository memberRepository;

    // 캘린더 조회
    public List<ReservationCountDTO> getReservationsCount(Date date, Long loginedRestaurantNo) {

        int month = date.toLocalDate().getMonthValue();
        int year = date.toLocalDate().getYear();

        log.info("🍎service month: {}, year : {}, restaurantNo : {} ", month, year, loginedRestaurantNo);

//        return reservationAdminRepository.countRerservationsByMonth(month, year, loginedRestaurantNo);
        return reservationAdminRepository.countReservationsByMonth(loginedRestaurantNo);
    }



    public List<ReservationDTO> getReservationInfos(Date parseDate, Long loginedRestaurantNo) {

        LocalDate date = parseDate.toLocalDate();

        return reservationAdminRepository.findByReservationDateAndRestaurantInfo(date, loginedRestaurantNo);
    }


    @Transactional
    public String cancelReservation(Integer reservationNo) {

        Reservation reservation = reservationAdminRepository.findById(reservationNo)
                .orElseThrow(() -> new RuntimeException("해당하는 예약 정보가 없습니다!"));

//        reservation.cancel();
//        reservationAdminRepository.save(reservation);

        reservationAdminRepository.deleteById(reservationNo);

        return "예약이 취소되었습니다!";
    }


    @Transactional
    public String enterReservation(Integer reservationNo) {

        Reservation reservation = reservationAdminRepository.findById(reservationNo)
                .orElseThrow(() -> new RuntimeException("해당하는 예약 정보가 없습니다!"));

        reservation.enter();
        reservation.getMember().increaseWeight(3.0);
        reservationAdminRepository.save(reservation);

        return "방문처리 완료!";
    }
}

