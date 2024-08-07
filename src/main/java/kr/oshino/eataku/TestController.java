package kr.oshino.eataku;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/myInfo/review")
    public String myReviewPage() {
        return "member/review";
    }

    @GetMapping("/myInfo")
    public String myPage() {
        return "member/myPage";
    }

    @GetMapping("/myInfo/profile")
    public String myProfilePage() {
        return "member/profile";
    }

    // List
    @GetMapping("/list")
    public String list() {
        return "list/list";
    }

    @GetMapping("/review")
    public String review() {
        return "list/review";
    }

    @GetMapping("/reviewDetail")
    public String reviewDetail() {
        return "list/reviewDetail";
    }

    @GetMapping("/test-list")
    public String testList() {
        return "list/test";
    }

    // Management
    @GetMapping("/address")
    public String address() {
        return "restaurant/address";
    }

    @GetMapping("/certification")
    public String certification() {
        return "restaurant/certification";
    }

    @GetMapping("/findId")
    public String findId() {
        return "restaurant/findId";
    }

    @GetMapping("/findPass")
    public String findPass() {
        return "restaurant/findPass";
    }

    @GetMapping("/login")
    public String login() {
        return "restaurant/login";
    }

    @GetMapping("/main")
    public String main() {
        return "restaurant/main";
    }

    @GetMapping("/menu")
    public String menu() {
        return "restaurant/menu";
    }

    @GetMapping("/reservationCheck")
    public String reservationCheck() {
        return "restaurant/reservationCheck";
    }

    @GetMapping("/reservationStatus")
    public String reservationStatus() {
        return "restaurant/reservationStatus";
    }

    @GetMapping("/reviewList")
    public String reviewList() {
        return "restaurant/reviewList";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "restaurant/signUp";
    }

    @GetMapping("/test-management")
    public String testManagement() {
        return "restaurant/test";
    }

    @GetMapping("/waitingStatus")
    public String waitingStatus() {
        return "restaurant/waitingStatus";
    }

    // Reservation
    @GetMapping("/reservationCalendar")
    public String reservationCalendar() {
        return "reservation/reservationCalendar";
    }

    @GetMapping("/reservationComplete")
    public String reservationComplete() {
        return "reservation/reservationComplete";
    }

    @GetMapping("/reservationDetail")
    public String reservationDetail() {
        return "reservation/reservationDetail";
    }

    @GetMapping("/reservationLogin")
    public String reservationLogin() {
        return "reservation/reservationLogin";
    }

    @GetMapping("/reservationLoginEmail")
    public String reservationLoginEmail() {
        return "member/signUp";
    }

    @GetMapping("/updateReservation")
    public String updateReservation() {
        return "reservation/updateReservation";
    }

    @GetMapping("/find")
    public String find() { return "member/find"; }
    @GetMapping("/userFindId")
    public String findId2() { return "member/findId"; }
    @GetMapping("/findPw")
    public String findPw() { return "member/findPw"; }
    @GetMapping("/userLogin")
    public String userLogin() { return "member/login"; }
    @GetMapping("/myPage")
    public String myPage2() { return "member/myPage"; }

    @GetMapping("/cardSearchPage")
    public String cardSearchPage() { return "waiting/cardSearchPage"; }
    @GetMapping("/jjFriendPage")
    public String jjFriendPage() { return "waiting/jjFriendPage"; }
    @GetMapping("/mapSearchPage")
    public String mapSearchPage() { return "waiting/mapSearchPage"; }
    @GetMapping("/userProfilePage")
    public String userProfilePage() { return "waiting/userProfilePage"; }
    @GetMapping("/waitingCardSearchPage")
    public String waitingCardSearchPage() { return "waiting/waitingCardSearchPage"; }

    @GetMapping("/member/history")
    public String history() { return "member/history"; }
    @GetMapping("/member/myList")
    public String myList() { return "member/myList"; }
    @GetMapping("/member/profile")
    public String profile() { return "member/profile"; }
    @GetMapping("/member/review")
    public String review2() { return "member/review"; }
}
