package kr.oshino.eataku.reservation.admin.controller;

import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationDTO;
import kr.oshino.eataku.reservation.admin.service.ReservationAdminService;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    // 예약 조회
    @GetMapping("/reservationCheck")
    public List<ReservationCountDTO> getReservationsCount(@RequestParam(value = "date", required = false) String date) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        LocalDate parseData;

        if(date == null || date == "") {
            parseData = LocalDate.now();
        }
        else {
            parseData = LocalDate.parse(date);
        }

        log.info("\uD83C\uDF4E controller date: {}", parseData);

        return reservationAdminService.getReservationsCount(parseData, loginedRestaurantNo);
    }

}
