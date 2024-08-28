package kr.oshino.eataku.reservation.admin.controller;

import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationCountDTO;
import kr.oshino.eataku.reservation.admin.model.dto.ReservationDTO;
import kr.oshino.eataku.reservation.admin.service.ReservationAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
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
        return "reservation/admin/reservationCheck";
    }


    /**
     * 날짜별 예약 정보 조회
     * @param date
     * @return
     */
    @GetMapping("/reservationInfo")
    @ResponseBody
    public ResponseEntity<List<ReservationDTO>> getReservationInfoByDate(@RequestParam("date") String date) {
        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        Date parseDate = Date.valueOf(date);
        log.info("parseDate = {}", parseDate);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationAdminService.getReservationInfos(parseDate, loginedRestaurantNo));
    }


    /**
     * 예약 취소 (매장)
     * @param reservationNo
     * @return
     */
    @PatchMapping("/reservation/cancel/{reservationNo}")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(@PathVariable Integer reservationNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationAdminService.cancelReservation(reservationNo));
    }


    /**
     * 예약 방문 처리 (매장)
     * @param reservationNo
     * @return
     */
    @PatchMapping("/reservation/enter/{reservationNo}")
    @ResponseBody
    public ResponseEntity<?> enterReservation(@PathVariable Integer reservationNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationAdminService.enterReservation(reservationNo));
    }



    /**
     * 캘린더에 표시할 예약 건수 조회
     * @param date
     * @return
     */
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
