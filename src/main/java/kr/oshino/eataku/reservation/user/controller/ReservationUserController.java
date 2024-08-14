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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;



@Slf4j
@Controller
//@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReservationUserController {

    private final ReservationUserService reservationUserService;


    /***
     * 예약 등록 페이지 이동 메서드
     */
    @GetMapping("/reservation/{restaurantNo}")
    public String reservation(@PathVariable String restaurantNo, Model model) {

        model.addAttribute("restaurantNo", restaurantNo);
        System.out.println("예약 페이지 접속");
        return "reservation/reservationCalendar";
    }


    /***
     * 특정 식당의 시간을 가져오는 메서드
     * @param restaurantNo
     * @return
     */
    @ResponseBody
    @GetMapping("/reservation/{restaurantNo}/available-times")
    public List<Map<String, Object>> getAvailableTimeSlots(
            @PathVariable("restaurantNo") Long restaurantNo,
            @RequestParam("date") String dateStr,
            @RequestParam("partySize") int partySize) {

        LocalDate date = LocalDate.parse(dateStr);  // 명시적으로 변환
        System.out.println("partySize = " + partySize);
        System.out.println("restaurantNo = " + restaurantNo);
        System.out.println("Received date: " + date); // 디버깅을 위한 로그
        return reservationUserService.getAvailableTimeSlots(date, restaurantNo, partySize);
    }


    /***
     * 예약 등록 메서드
     * @param createReservationUserRequestDto
     * @return
     */
    @PostMapping("/reservation")
    @ResponseBody
    public ResponseEntity<CreateReservationUserResponseDto> registerReservation(@RequestBody CreateReservationUserRequestDto createReservationUserRequestDto) {
        log.info("CreateReservationUserRequestDto: {}", createReservationUserRequestDto);
        System.out.println("createReservationUserRequestDto 확인!!! = " + createReservationUserRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationUserService.registerReservation(createReservationUserRequestDto));
    }




    /***
     * 예약한 인원수 만큼 예약세팅 베이블에서 빼기
     */
    @PutMapping("/reservation/{reservationNo}/subtract")
    public ResponseEntity<Void> subtractPartySize(@PathVariable Long reservationNo,
                                                  @RequestParam int partySize,
                                                  @RequestParam String time) {
        try {
            LocalTime parsedTime = LocalTime.parse(time);
            reservationUserService.subtractPartySize(reservationNo, partySize, parsedTime);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("인원수 제거 에러남", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}


