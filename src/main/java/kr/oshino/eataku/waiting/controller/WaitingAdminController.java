package kr.oshino.eataku.waiting.controller;

import kr.oshino.eataku.waiting.event.WaitingCreatedEvent;
import kr.oshino.eataku.waiting.model.dto.requestDto.ReadWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.requestDto.UpdateWaitingRequestDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.ReadWaitingResponseDto;
import kr.oshino.eataku.waiting.model.dto.responseDto.UpdateWaitingResponseDto;
import kr.oshino.eataku.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WaitingAdminController {
    // 관리자 전용 컨트롤러

    private final WaitingService waitingService;
    private final Map<Long, Sinks.Many<Long>> waitingSinks;





    /**
     * 웨이팅 관리 페이지 이동
     * @return
     */
    @GetMapping("/waitingList")
    public String waitingManagementPage(Model model) {

        // 나중에 세션으로 변경해야 함
        Long restaurantNo = 3L;
        model.addAttribute("restaurantNo", restaurantNo);

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




    /**
     * 웨이팅 입장 메세지 전송
     * @param waitingNo
     * @return
     */
    @PostMapping("/waiting/sendOne/{waitingNo}")
    @ResponseBody
    public SingleMessageSentResponse sendEntryMessage(@PathVariable Long waitingNo) {
        return waitingService.sendWaitingEntryMessage(waitingNo);
    }





    @EventListener
    public void handleWaitingCreatedEvent(WaitingCreatedEvent event) {
        Long restaurantNo = event.getRestaurantNo();

        Sinks.Many<Long> sink = waitingSinks.get(restaurantNo);
        if (sink != null) {
            sink.tryEmitNext(restaurantNo);
        }
    }



    /**
     * 실시간 웨이팅 스트림
     * @return
     */
    @GetMapping(value = "/waiting/stream/{restaurantNo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Long> streamWaiting(@PathVariable Long restaurantNo) {
        waitingSinks.putIfAbsent(restaurantNo, Sinks.many().multicast().onBackpressureBuffer());
        return waitingSinks.get(restaurantNo).asFlux();
    }
}
