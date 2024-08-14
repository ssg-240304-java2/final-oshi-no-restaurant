package kr.oshino.eataku.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.oshino.eataku.member.model.dto.MemberDTO;
import kr.oshino.eataku.member.service.MailService;
import kr.oshino.eataku.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/signUp")
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
}
