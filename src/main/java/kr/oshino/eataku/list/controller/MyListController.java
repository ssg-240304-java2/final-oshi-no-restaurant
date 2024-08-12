package kr.oshino.eataku.list.controller;

import kr.oshino.eataku.list.model.service.MyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


}




