package kr.oshino.eataku.waiting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class WaitingAdminController {

    /**
     * 웨이팅 관리 페이지 이동
     * @return
     */
    @GetMapping("/waitingList")
    public String waitingManagementPage() {

        return "restaurant/waitingStatus";
    }

    // 웨이팅 조회 (Get)

    // 웨이팅 "입장 완료" 처리 (Patch)

    // 웨이팅 "취소" 처리 (Patch)

    // 웨이팅 호출 버튼..
}
