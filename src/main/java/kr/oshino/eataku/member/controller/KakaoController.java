package kr.oshino.eataku.member.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.dto.KakaoUserInfoResponseDTO;
import kr.oshino.eataku.member.model.dto.MemberDTO;
import kr.oshino.eataku.member.service.CustomMemberDetailsService;
import kr.oshino.eataku.member.service.KakaoService;
import kr.oshino.eataku.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final CustomMemberDetailsService customMemberDetailsService;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, HttpServletRequest request) {

        log.info(" 🚥🚥 [ KakaoController ] code : {} 🚥🚥", code);

        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        log.info(" 🚥🚥 [ KakaoController ] token : {} 🚥🚥", accessToken);

        KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(accessToken);

        log.info(" 🚥🚥 [ KakaoController ] userInfo : {} 🚥🚥", userInfo);

        boolean isSignUp = memberService.selectSignUpByIdAndNameAndEmail(String.valueOf(userInfo.id), userInfo.getKakaoAccount().profile.nickName, userInfo.getKakaoAccount().email);

        try {
            if (!isSignUp) {
                MemberDTO member = new MemberDTO();
                member.setImgUrl(userInfo.getKakaoAccount().profile.profileImageUrl);
                member.setEmail(userInfo.getKakaoAccount().email);
                member.setAccount(String.valueOf(userInfo.id));
                member.setPassword("");
//            member.setAuth("ROLE_GENERAL"); 서비스에서 하드코딩
                member.setName(userInfo.getKakaoAccount().profile.nickName);
                member.setNickname(userInfo.getKakaoAccount().email);
                memberService.insertNewMember(member);
            }

            CustomMemberDetails memberDetails = (CustomMemberDetails) customMemberDetailsService.loadUserByUsername(String.valueOf(userInfo.id));
            log.info("🚥 memberDetails : {} 🚥", memberDetails.toString());

            // 3. Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
            log.info("🚥 authentication : {} 🚥", authentication.getPrincipal().toString());

            // 4. SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);


            // SecurityContext를 세션에 저장
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location","/")
                    .build();
        } catch (Exception e) {
            log.error(" 🚥🚥 [ KakaoController ] 로그인 처리 중 오류 발생 🚥🚥", e);
            // 로그인 실패 시 리다이렉트
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location","/login?error=true")
                    .build();
        }
    }
}