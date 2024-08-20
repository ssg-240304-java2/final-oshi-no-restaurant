package kr.oshino.eataku.reservation.user.controller;
import kr.oshino.eataku.reservation.user.model.dto.requestDto.CreateReservationUserRequestDto;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.*;
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
        System.out.println("restaurantNo = " + restaurantNo);
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

        LocalDate date = LocalDate.parse(dateStr);
        System.out.println("partySize = " + partySize);
        System.out.println("restaurantNo = " + restaurantNo);
        System.out.println("Received date: " + date);
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

    /***
     * 모달창에 상세정보
     */
    @GetMapping("/reservation/{restaurantNo}/modal")
    public String modal(@PathVariable String restaurantNo, Model model) {
        // 모델에 필요한 데이터를 추가
        model.addAttribute("restaurantNo", restaurantNo);
        return "reservation/reservationComplete";  // 이 뷰 페이지가 실제로 모달을 포함하고 있어야 함
    }

    /***
     * 모달에 대한 상세정보 반환
     * @param restaurantNo
     * @return
     */

    @PostMapping("/reservation/{restaurantNo}/modal/data")
    @ResponseBody
    public ResponseEntity<modalDto> getModalDetails(@PathVariable Long restaurantNo) {

        modalDto modalDetails = reservationUserService.getModalDetails(restaurantNo);

        System.out.println("modalDetails = " + modalDetails);
        return ResponseEntity.ok(modalDetails);
    }

    /***
     * 날짜 가져오기
     */
    @PostMapping("/reservation/{restaurantNo}/available-dates")
    @ResponseBody
    public List<LocalDate> getAvailableDates(@PathVariable Long restaurantNo) {

        return reservationUserService.getAvailableDates(restaurantNo);
    }


    /***
     * 예약 조회
     */
    @GetMapping("/reservation")
    @ResponseBody
    public ResponseEntity<List<ReadReservationResponseDto>> getMyreservationList(
            ReadReservationResponseDto readReservationResponseDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationUserService.getReservationListByMemberNo(readReservationResponseDto));
    }

    /***
     * 예약 취소
     */
    @GetMapping("/reservation/cancel")

    public String cancelReservations() {

        return "reservation/updateReservation";
    }

    /***
     * 예약취소
     * @param reservationNo
     * @return
     */

    @PostMapping("/reservation/{reservationNo}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(@PathVariable int reservationNo) {
        boolean isCancelled = reservationUserService.cancelReservation(reservationNo);
        System.out.println("isCancelled = " + isCancelled);

        if (isCancelled) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "예약이 성공적으로 취소되었습니다."));
        } else {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "예약 취소에 실패하였습니다."));
        }
    }

    /***
     * 식당 상세 페이지
     */

    /**
     * 식당 상세 페이지를 로드
     */
    @GetMapping("/detail")
    public String detail() {
        return "reservation/reservationDetail";
    }

    /**
     * 특정 식당의 상세 정보 가져오기
     */
    @GetMapping("/detail/{restaurantNo}/detailPage")
    public String detailPage(@PathVariable Long restaurantNo, Model model) {
        RestaurantInfoDetails restaurant = reservationUserService.getRestaurantDetailsByReservation(restaurantNo);
        model.addAttribute("restaurant", restaurant);
        List<ReviewDetails> reviewDetails = reservationUserService.getReviewDetails(restaurantNo);
        model.addAttribute("ReviewDetails", reviewDetails);
        System.out.println("reviewDetails = " + reviewDetails);
        System.out.println("restaurant = " + restaurant);
        return "reservation/reservationDetail";
    }

}


