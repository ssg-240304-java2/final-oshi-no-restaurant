package kr.oshino.eataku.waiting.controller;

import kr.oshino.eataku.waiting.model.dto.requestDto.ReadWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.requestDto.UpdateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.UpdateWaitingResponseDto;
import kr.oshino.eataku.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WaitingAdminController {
    // 관리자 전용 컨트롤러

    private final WaitingService waitingService;





    /**
     * 웨이팅 관리 페이지 이동
     * @return
     */
    @GetMapping("/waitingList")
    public String waitingManagementPage() {
        return "restaurant/waitingStatus";
    }





    /**
     * 웨이팅 조회 (매장)
     * @param readWaitingRequestDto
     * @return
     */
    @GetMapping("/waiting")
    @ResponseBody
    public ResponseEntity<List<ReadWaitingResponseDto>> getWaitingList(
            @ModelAttribute ReadWaitingRequestDto readWaitingRequestDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.getWaitingListByRestaurantNo(readWaitingRequestDto));
    }





    /**
     * 웨이팅 입장 처리
     * @param updateWaitingRequestDto
     * @return
     */
    @PatchMapping("/waiting/visit")
    @ResponseBody
    public ResponseEntity<UpdateWaitingResponseDto> updateWaitingStatus(
            @RequestBody UpdateWaitingRequestDto updateWaitingRequestDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.updateWaitingByWaitingNo(updateWaitingRequestDto));
    }





    /**
     * 웨이팅 취소
     * @param updateWaitingRequestDto
     * @return
     */
    @PatchMapping("/waiting/cancel")
    @ResponseBody
    public ResponseEntity<UpdateWaitingResponseDto> cancelWaitingStatus(
            @RequestBody UpdateWaitingRequestDto updateWaitingRequestDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.cancelWaitingByWaitingNo(updateWaitingRequestDto));
    }
}
