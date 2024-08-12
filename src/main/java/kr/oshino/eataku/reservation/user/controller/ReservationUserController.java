package kr.oshino.eataku.reservation.user.controller;
import kr.oshino.eataku.reservation.user.model.dto.requestDto.CreateReservationUserRequestDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.CreateReservationUserResponseDto;
import kr.oshino.eataku.reservation.user.service.ReservationUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.*;


@Slf4j
@Controller
//@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReservationUserController {

    private final ReservationUserService reservationUserService;





    /***
     * 예약 등록 페이지 이동 메서드
     */
    @GetMapping("/reservation")
    public String reservation() {
        System.out.println("예약 페이지 접속");
        return "reservation/reservationCalendar";
    }



    /***
     * 특정 식당의 정보를 가져오는 메서드
     * @param restaurantNo
     * @return ResponseEntity with the restaurant details
     */
        @GetMapping("/reservation/{restaurantNo}")
        public String getReservationPage(@PathVariable Long restaurantNo, Model model) {
            List<LocalTime> times = reservationUserService.getReservationTimes(restaurantNo);
            int maxPeople = reservationUserService.getMaxPeople(restaurantNo);

            List<Integer> peopleNumbers = new ArrayList<>();
            for (int i = maxPeople; i > 0; i--) {
                peopleNumbers.add(i);
            }

            model.addAttribute("reservationTimes", times);
            model.addAttribute("reservationPeople", peopleNumbers);
            System.out.println("peopleNumbers = " + peopleNumbers);
            System.out.println("times = " + times);

            return "reservation/reservationCalendar";
        }



    /***
     * 예약 등록 메서드
     * @param createReservationUserRequestDto
     * @return
     */
    @PostMapping("/reservation")
    public ResponseEntity<CreateReservationUserResponseDto> registerReservation(@RequestBody CreateReservationUserRequestDto createReservationUserRequestDto) {
        log.info("CreateReservationUserRequestDto: {}", createReservationUserRequestDto);
        System.out.println("createReservationUserRequestDto 확인!!! = " + createReservationUserRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationUserService.registerReservation(createReservationUserRequestDto));
    }


    /***
     * 예약 확정
     */
    @GetMapping("/reservation/complete")
    public String reservationComplete() {
        return "reservation/reservationComplete";
    }


    /***
     * 예약취소
     */
    @GetMapping("/reservation/cancel")
    public String updateReservation(Model model) {
        return "reservation/updateReservation";
    }



}
