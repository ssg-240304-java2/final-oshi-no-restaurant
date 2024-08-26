package kr.oshino.eataku.reservation.admin.service;

import kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationDTO;
import kr.oshino.eataku.reservation.admin.repository.ReservationAdminRepository;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationAdminService {

    ReservationAdminRepository reservationAdminRepository;

    // 캘린더 조회
    public List<ReservationCountDTO> getReservationsCount(LocalDate date, Long loginedRestaurantNo) {

        int month = date.getMonthValue();
        int year = date.getYear();

        log.info("🍎service month: {}, year : {}, restaurantNo : {} ", month, year, loginedRestaurantNo);

        return reservationAdminRepository.countRerservationsByMonth(month, year, loginedRestaurantNo);
    }

}

