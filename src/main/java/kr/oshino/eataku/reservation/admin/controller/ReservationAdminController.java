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
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    // 예약 조회
    @GetMapping("/reservationCheck")
    public String getReservationsCount() {
        return "restaurant/reservationCheck";
    }

    @PostMapping("/reservationCheck")
    @ResponseBody
    public List<ReservationCountDTO> getReservationInfo(@RequestParam(value = "date", defaultValue = "") String date) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        Date parseData;

        if(date == null || date.equals("")) {
            log.info("\uD83C\uDF4E controller date is null !!: {}", date);
            parseData = Date.valueOf(LocalDate.now());
        }
        else {
            log.info("\uD83C\uDF4E controller do parsing: {}", date);
            parseData = Date.valueOf(date);
        }

        log.info("\uD83C\uDF4E controller date: {}", parseData);

        List<ReservationCountDTO> result = reservationAdminService.getReservationsCount(parseData, loginedRestaurantNo);
        log.info("\uD83C\uDF4E controller result: {}", result);

        return result;
    }

}
