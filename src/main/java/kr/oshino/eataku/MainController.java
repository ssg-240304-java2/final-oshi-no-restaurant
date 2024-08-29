package kr.oshino.eataku;

import kr.oshino.eataku.search.model.dto.SearchResultDTO;
import kr.oshino.eataku.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final SearchService searchService;

    @GetMapping("/")
    public String main(Model model){

        // 추천 박사들이 인정한 식당
        List<SearchResultDTO> popularLists = searchService.selectPopularLists();
        log.info("👍👍 [ MainController ] popularLists : {} 👍👍", popularLists);

        // 실시간 인기 식당
        List<SearchResultDTO> busiestLists = searchService.selectBusiestLists();

        // 바로 입장 가능한 식당
        List<SearchResultDTO> directLists = searchService.selectDirectLists();


        model.addAttribute("popularLists", popularLists);
        model.addAttribute("busiestLists", busiestLists);
        model.addAttribute("directLists", directLists);

        return "index";
    }

}
