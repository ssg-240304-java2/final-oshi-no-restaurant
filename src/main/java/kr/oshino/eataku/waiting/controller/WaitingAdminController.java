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
     * 웨이팅 조회
     * @param restaurantNo
     * @return
     */
    @GetMapping("/waiting/{restaurantNo}")
    @ResponseBody
    public ResponseEntity<List<ReadWaitingResponseDto>> getWaitingList(
            @PathVariable Long restaurantNo) {

        ReadWaitingRequestDto readWaitingRequestDto =
                ReadWaitingRequestDto.builder().restaurantNo(restaurantNo).build();

        log.info("readWaitingRequestDto: {}", readWaitingRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.getWaitingListByRestaurantNo(readWaitingRequestDto));
    }





    /**
     * 웨이팅 입장 처리
     * @param waitingNo
     * @return
     */
    @PatchMapping("/waiting/visit/{waitingNo}")
    @ResponseBody
    public ResponseEntity<UpdateWaitingResponseDto> updateWaitingStatus(
            @PathVariable Long waitingNo) {

        UpdateWaitingRequestDto updateWaitingRequestDto =
                UpdateWaitingRequestDto.builder().waitingNo(waitingNo).build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.updateWaitingByWaitingNo(updateWaitingRequestDto));
    }





    /**
     * 웨이팅 취소
     * @param waitingNo
     * @return
     */
    @PatchMapping("/waiting/cancel/{waitingNo}")
    @ResponseBody
    public ResponseEntity<UpdateWaitingResponseDto> cancelWaitingStatus(
            @PathVariable Long waitingNo) {

        UpdateWaitingRequestDto updateWaitingRequestDto =
                UpdateWaitingRequestDto.builder().waitingNo(waitingNo).build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(waitingService.cancelWaitingByWaitingNo(updateWaitingRequestDto));
    }
}
