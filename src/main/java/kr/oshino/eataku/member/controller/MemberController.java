package kr.oshino.eataku.member.controller;

import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.entity.MemberLoginInfo;
import kr.oshino.eataku.member.model.dto.MemberDTO;
import kr.oshino.eataku.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
//@RequestMapping("/login")
//@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "member/login";
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
    public ResponseEntity<Integer> sendEmailVerifCode(@RequestBody MemberDTO member) {
        log.info("⭐️⭐️ [ MemberController ] Send to Email : {} ⭐️⭐️", member.getEmail());

        int verifCode = memberService.sendEmailVerifCode(member.getEmail(), null);
        log.info("⭐️⭐️ [ MemberController ] Email Send verifCode T/F : {} ⭐️⭐️", verifCode);

        return ResponseEntity.ok(verifCode);
    }

    @PostMapping("/signUp")
    public String signUpProc(MemberDTO member) {

        log.info("⭐️⭐️ [ MemberController ] SignUp MemberInfo : {} ⭐️⭐️", member);

        memberService.insertNewMember(member);

        return "redirect:/login";
    }
}
