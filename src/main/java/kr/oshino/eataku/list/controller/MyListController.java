package kr.oshino.eataku.list.controller;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.list.model.dto.FollowListDto;
import kr.oshino.eataku.list.model.service.MyListService;
import kr.oshino.eataku.list.model.vo.RestaurantList;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
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

    // 식당을 리스트에 추가 (좋아요 클릭 시)--- 나중에 수정
    @PostMapping("/{listNo}/addRestaurant")
    @ResponseBody
    public ResponseEntity<String> addRestaurantToList(@PathVariable("listNo") Integer listNo,
                                                      @RequestBody RestaurantList restaurant) {
        try {
            myListService.addRestaurantToList(listNo, restaurant);
            return ResponseEntity.ok("Restaurant added to the list successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add restaurant to list");
        }
    }


    // ajax 로 특정 리스트의 식당 정보를 가져오는 메소드
    @GetMapping("/tsktskLists/{listNo}/restaurants")
    @ResponseBody
    public List<RestaurantList> getRestaurantLists(@PathVariable Integer listNo) {
        log.info("리스트 번호: " + listNo + "의 식당 정보를 가져옵니다.");
        return myListService.getRestaurantListsByListNo(listNo);
    }

    // 식당 좌표 넘겨주기(지도에 마커 표시)
//    @GetMapping("/tsktskLists/restaurantCoordinates")
//    @ResponseBody
//    public List<RestaurantList> getRestaurantCoordinates() {
//        // 데이터베이스에서 모든 좌표와 식당 정보를 가져오는 메서드
//        return myListService.getAllRestaurantCoordinates();
//    }

    // 식당 삭제 메소드
    @PostMapping("/tsktskLists/deleteRestaurants")
    public String deleteRestaurants(@RequestParam(value = "restaurantNos", required = false) List<Long> restaurantNos,
                                    @RequestParam("listNo") Integer listNo,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (restaurantNos != null && !restaurantNos.isEmpty()) {
                myListService.deleteRestaurants(listNo, restaurantNos);
                redirectAttributes.addFlashAttribute("message", "식당 정보가 삭제되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("error", "삭제할 식당을 선택하지 않았습니다.");
            }
            return "redirect:/tsktskLists";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "삭제 중 오류가 발생했습니다.");
            return "redirect:/tsktskLists";
        }
    }


    // 내 쩝쩝 리스트 페이지를 띄우는 메소드 + 팔로우 리스트
    @GetMapping("/tsktskLists")
    public String showMyLists(Model model) {
        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedMemberNo = member.getMemberNo();

        List<MyList> myLists = myListService.getAllMyLists();
        model.addAttribute("myLists", myLists);

        // 팔로잉 리스트 추가
        List<MyList> followingLists = myListService.getFollowingLists(loginedMemberNo);
        model.addAttribute("followingLists", followingLists);

        log.info("⭐⭐ [ 값 주나? ] Request followingLists: {} ⭐⭐", followingLists);

        return "list/tsktskList";
    }



}





