package kr.oshino.eataku.list.controller;

import kr.oshino.eataku.list.model.service.MyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MyListController {

    private final MyListService myListService;

    @GetMapping("/myInfo/list")
    public String showLists(Model model) {
        model.addAttribute("myLists", myListService.getLists());
        return "member/myList";
    }

    @GetMapping("/myLists/create")
    public String showCreateListForm() {
        return "list/listCreate";
    }

    @PostMapping("/myLists/createLists")
    public String createList(@RequestParam("listName") String listName) {
        myListService.createList(listName);
        return "redirect:/myInfo/list";
    }

    // 리스트 제목 수정을 위한 폼 표시
    @GetMapping("/myLists/{listNo}/edit")
    public String showEditListForm(@PathVariable("listNo") int listNo, Model model) {
        myListService.modifyList(listNo);
        return "list/listModify";
    }

    // 리스트 제목 수정을 처리
    @PostMapping("/myLists/{listNo}/edit")
    public String editListTitle(@PathVariable("listNo") int listNo, @RequestParam("listName") String listName) {
        boolean updated = myListService.updateListTitle(listNo, listName);

        if (!updated) {
            return "redirect:/myInfo/list";
        }

        return "redirect:/myInfo/list";
    }
}
