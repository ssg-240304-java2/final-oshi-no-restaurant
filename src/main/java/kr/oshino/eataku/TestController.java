package kr.oshino.eataku;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
public class TestController {

    @GetMapping("/myInfo/review")
    public String myReviewPage() {
        return "user/review";
    }

    @GetMapping("/myInfo")
    public String myPage() {
        return "user/myPage";
    }

    @GetMapping("/myInfo/profile")
    public String myProfilePage() {
        return "user/profile";
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
        return "management/address";
    }

    @GetMapping("/certification")
    public String certification() {
        return "management/certification";
    }

    @GetMapping("/findId")
    public String findId() {
        return "management/findId";
    }

    @GetMapping("/findPass")
    public String findPass() {
        return "management/findPass";
    }

    @GetMapping("/login")
    public String login() {
        return "management/login";
    }

    @GetMapping("/main")
    public String main() {
        return "management/main";
    }

    @GetMapping("/menu")
    public String menu() {
        return "management/menu";
    }

    @GetMapping("/reservationCheck")
    public String reservationCheck() {
        return "management/reservationCheck";
    }

    @GetMapping("/reservationStatus")
    public String reservationStatus() {
        return "management/reservationStatus";
    }

    @GetMapping("/reviewList")
    public String reviewList() {
        return "management/reviewList";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "management/signUp";
    }

    @GetMapping("/test-management")
    public String testManagement() {
        return "management/test";
    }

    @GetMapping("/waitingStatus")
    public String waitingStatus() {
        return "management/waitingStatus";
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
        return "reservation/reservationLoginEmail";
    }

    @GetMapping("/updateReservation")
    public String updateReservation() {
        return "reservation/updateReservation";
    }

    @GetMapping("/find")
    public String find() { return "user/find"; }
    @GetMapping("/userFindId")
    public String findId2() { return "user/findId"; }
    @GetMapping("/findPw")
    public String findPw() { return "user/findPw"; }
    @GetMapping("/userLogin")
    public String userLogin() { return "user/login"; }
    @GetMapping("/myPage")
    public String myPage() { return "user/myPage"; }

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
}
