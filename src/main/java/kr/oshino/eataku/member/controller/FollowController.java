package kr.oshino.eataku.member.controller;

import kr.oshino.eataku.list.entity.MyList;
import kr.oshino.eataku.member.entity.Member;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.member.model.dto.ZzupListDTO;
import kr.oshino.eataku.member.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;

    @GetMapping("/zzupFriend")
    public String zzupFriendPage(@RequestParam(value = "query",defaultValue = "") String query, Model model) {

        log.info("⭐⭐ [ FollowController ] Request query: {} ⭐⭐", query);

        List<MyList> queryResult = followService.findByListNameContainingAndListStatusOrderByListShareDesc(query, "Public");
        List<ZzupListDTO> memberLists = new ArrayList<>();

        if (queryResult != null || !queryResult.isEmpty()) {
            memberLists = queryResult
                    .stream()
                    .map(entity -> new ZzupListDTO(entity.getListNo()
                    ,entity.getListName()
                    ,entity.getListShare()
                    ,entity.getMember().getMemberNo()
                    ,entity.getMember().getName()
                    ,entity.getMember().getImgUrl()))
                    .toList();
        }

        model.addAttribute("memberLists", memberLists);


        return "member/zzupFriend";
    }

    @PostMapping("/follow")
    public ResponseEntity<Boolean> followToggle(@RequestBody Member member) {

        CustomMemberDetails logined = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedMemberNo = logined.getMemberNo();

        Boolean followed = followService.toggleFollowStatus(loginedMemberNo, member.getMemberNo());

        return ResponseEntity.ok(followed);
    }
}
