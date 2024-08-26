package kr.oshino.eataku.reservation.admin.service;

import kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationDTO;
import kr.oshino.eataku.reservation.admin.repository.ReservationAdminRepository;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationAdminService {

    private final ReservationAdminRepository reservationAdminRepository;

    // 캘린더 조회
    public List<ReservationCountDTO> getReservationsCount(Date date, Long loginedRestaurantNo) {

        int month = date.toLocalDate().getMonthValue();
        int year = date.toLocalDate().getYear();

        log.info("🍎service month: {}, year : {}, restaurantNo : {} ", month, year, loginedRestaurantNo);

        return reservationAdminRepository.countRerservationsByMonth(month, year, loginedRestaurantNo);
    }

}

