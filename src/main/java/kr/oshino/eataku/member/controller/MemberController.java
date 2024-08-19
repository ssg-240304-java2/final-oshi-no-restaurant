package kr.oshino.eataku.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.dto.MemberDTO;
import kr.oshino.eataku.member.model.dto.MemberProfileDTO;
import kr.oshino.eataku.member.model.dto.MyInfoDTO;
import kr.oshino.eataku.member.service.MailService;
import kr.oshino.eataku.member.service.MemberService;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantAccountInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
//@RequestMapping("/login")
//@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/managerLogin")
    public String managerLogin() { return "member/managerLogin"; }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/signUp/general")
    public String signUpPage() {
        return "member/signUp";
    }

    @PostMapping("/signUp/checkDupAccount")
    public ResponseEntity<Boolean> checkDuplicateAccount(@RequestBody MemberDTO memberLoginInfo) {
        log.info("⭐️⭐️ [ MemberController ] Check Duplicate account : {} ⭐️⭐️", memberLoginInfo.getAccount());

        boolean isDuplicate = memberService.checkDuplicateAccount(memberLoginInfo.getAccount());
        log.info("⭐️⭐️ [ MemberController ] Check Duplicate account : {} ⭐️⭐️", isDuplicate);

        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/signUp/checkDupNickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(@RequestBody MemberDTO member) {
        log.info("⭐️⭐️ [ MemberController ] Check Duplicate nickname : {} ⭐️⭐️", member.getNickname());

        boolean isDuplicate = memberService.checkDuplicateNickname(member.getNickname());
        log.info("⭐️⭐️ [ MemberController ] Check Duplicate nickname : {} ⭐️⭐️", isDuplicate);

        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/signUp/sendEmailVerifCode")
    public ResponseEntity<Boolean> sendEmailVerifCode(@RequestBody MemberDTO member) {
        log.info("⭐️⭐️ [ MemberController ] Send to Email : {} ⭐️⭐️", member.getEmail());

        int verifCode = mailService.sendEmailVerifCode(member.getEmail(), null);
        log.info("⭐️⭐️ [ MemberController ] Email Send verifCode T/F : {} ⭐️⭐️", verifCode);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/signUp/checkEmailVerifCode")
    public ResponseEntity<Boolean> checkEmailVerifCode(@RequestBody Map<String,String> request) {
        String reqVerifCode = request.get("reqVerifCode");
        String email = request.get("email");

        log.info("⭐️⭐️ [ MemberController ] Request Email : {}, VerifCode : {} ⭐️⭐️", email, reqVerifCode);

        boolean isMatch = mailService.checkMailVerifCode(email, reqVerifCode);

        return ResponseEntity.ok(isMatch);
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUpProc(@RequestBody  MemberDTO member) {

        log.info("⭐️⭐️ [ MemberController ] SignUp MemberInfo : {} ⭐️⭐️", member);

        memberService.insertNewMember(member);

        return ResponseEntity.ok("/login");
    }

    @GetMapping("/signUp/manager")
    public String managerSignUpProc() {
        return "member/managerSignUp";

    }

    @PostMapping("/signUp/manager")
    public ResponseEntity<String> managerSignUpProc(@RequestBody  RestaurantAccountInfoDTO member, HttpSession session) {

        log.info("⭐️⭐️ [ MemberController ] SignUp Restaurant : {} ⭐️⭐️", member);

        session.setAttribute("id", member.getId());
        session.setAttribute("password", member.getPassword());
        session.setAttribute("email", member.getEmail());

        return ResponseEntity.ok("/managerLogin");
    }

    @GetMapping("/member/{memberNo}")
    public String memberInfo(@PathVariable("memberNo") Long memberNo, Model model) {

        MemberProfileDTO member = memberService.selectProfile(memberNo);

        model.addAttribute("member", member);

        return "member/memberProfile";
    }

    @GetMapping("/myPage")
    public String myPage(Model model) {

        CustomMemberDetails logginedMember = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long logginedMemberNo = logginedMember.getMemberNo();

        // 프로필 정보 조회
        MyInfoDTO member = memberService.selectMyProfile(logginedMemberNo);

        model.addAttribute("member", member);

        return "member/myPage";
    }

    @GetMapping("/myInfo")
    public String myInfo(Model model) {

        CustomMemberDetails logginedMember = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long logginedMemberNo = logginedMember.getMemberNo();

        Member member = memberService.selectMyData(logginedMemberNo);

        model.addAttribute("member", member);

        return "member/editProfile";
    }

    @PutMapping("/myInfo")
    public ResponseEntity<Boolean> modifyProfile(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart("jsonData") MemberDTO member){

        CustomMemberDetails logginedMember = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long logginedMemberNo = logginedMember.getMemberNo();

        log.info("⭐️⭐️ [ MemberController ] modifyProfile file : {}, member : {} ⭐️⭐️", file.isEmpty() , member);

        boolean isSuccess = memberService.updateProfile(file, member, logginedMemberNo);

        if(isSuccess){
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.ok(false);
    }
}
