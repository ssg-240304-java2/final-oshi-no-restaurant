package kr.oshino.eataku.waiting.controller;

import kr.oshino.eataku.waiting.model.dto.requestDto.CreateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.requestDto.ReadWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.requestDto.UpdateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.CreateWaitingResponseDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.UpdateWaitingResponseDto;
import kr.oshino.eataku.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WaitingUserController {
    // 회원 전용 컨트롤러

    private final WaitingService waitingService;





    /**
     * 웨이팅 등록 페이지 이동
     * @param restaurantNo
     * @param model
     * @return
     */
    @GetMapping("/waitingForm/{restaurantNo}")
    public String waitingFormPage(@PathVariable int restaurantNo, Model model) {

        model.addAttribute("restaurantNo", restaurantNo);
        // 여기서 회원 정보도 가지고 들어가야 하는지 판별해야 함.
        return "waiting/waitingFormPage";
    }





    /**
     * 웨이팅 등록
     * @param createWaitingRequestDto
     * @return
     */
    @PostMapping("/waiting")
    @ResponseBody
    public ResponseEntity<CreateWaitingResponseDto> registerWaiting(
            @RequestBody CreateWaitingRequestDto createWaitingRequestDto) {

        log.info("createWaitingRequestDto: {}", createWaitingRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.registerWaiting(createWaitingRequestDto));
    }





    /**
     * 웨이팅 조회
     * @param readWaitingRequestDto
     */
    @GetMapping("/waiting")
    @ResponseBody
    public ResponseEntity<List<ReadWaitingResponseDto>> getMyWaitingList(
            ReadWaitingRequestDto readWaitingRequestDto) {

        log.info("readWaitingRequestDto: {}", readWaitingRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.getWaitingListByMemberNo(readWaitingRequestDto));
    }





    // 웨이팅 취소
    @PatchMapping("/waiting")
    @ResponseBody
    public ResponseEntity<UpdateWaitingResponseDto> cancelWaiting(
            @RequestBody UpdateWaitingRequestDto updateWaitingRequestDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.cancelWaitingByWaitingNo(updateWaitingRequestDto));
    }
}
