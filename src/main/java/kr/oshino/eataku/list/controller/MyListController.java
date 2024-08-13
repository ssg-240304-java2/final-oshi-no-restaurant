package kr.oshino.eataku.list.controller;

import kr.oshino.eataku.list.model.service.MyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MyListController {

    private final MyListService myListService;

    // 리스트 관리 페이지 이동
    @GetMapping("/myInfo/list")
    public String showLists(Model model) {
        model.addAttribute("myLists", myListService.getLists());
        return "member/myList";
    }

    // 리스트 생성
    @GetMapping("/myLists/create")
    public String showCreateListForm() {
        return "list/listCreate";
    }

    @PostMapping("/myLists/createLists")
    public String createList(@RequestParam("listName") String listName) {
        myListService.createList(listName);
        return "redirect:/myInfo/list";
    }

    // 리스트 제목 삭제
    @PostMapping("/myLists/delete/{listNo}")
    public String deleteList(@PathVariable("listNo") Integer listNo, RedirectAttributes redirectAttributes) {
        myListService.deleteList(listNo);
        redirectAttributes.addFlashAttribute("message", "리스트가 성공적으로 삭제되었습니다.");
        return "redirect:/myInfo/list";
    }

    // 리스트 제목 수정
    @PostMapping("/myLists/modify")
    public String modifyListTitle(@RequestParam("listNo") String listNoStr,
                                  @RequestParam("newListName") String newListName,
                                  RedirectAttributes redirectAttributes) {
        Integer listNo = Integer.valueOf(listNoStr);
        myListService.modify(listNo, newListName);
        redirectAttributes.addFlashAttribute("message", "리스트 이름이 성공적으로 수정되었습니다.");
        return "redirect:/myInfo/list";
    }

    // 리스트 공개 상태 변경
    @PostMapping("/myLists/updateStatus")
    @ResponseBody
    public ResponseEntity<String> updateListStatus(@RequestBody Map<String, String> payload) {
        try {
            // 요청에서 listNo와 listStatus 추출
            String listNoStr = payload.get("listNo");
            String listStatus = payload.get("listStatus");

            // 수신된 데이터 로그로 출력
            System.out.println("Received listNo: " + listNoStr);
            System.out.println("Received listStatus: " + listStatus);

            // listNo의 유효성 검사
            if (listNoStr == null || listNoStr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("listNo is missing or empty");
            }

            // listNo를 Integer로 변환 및 유효성 검사
            Integer listNo;
            try {
                listNo = Integer.valueOf(listNoStr);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("listNo is not a valid integer");
            }

            // listStatus의 유효성 검사
            if (listStatus == null || listStatus.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("listStatus is missing or empty");
            }

            // 서비스 호출하여 상태 업데이트
            myListService.updateStatus(listNo, listStatus);

            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 500 오류 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update status");
        }
    }

}




