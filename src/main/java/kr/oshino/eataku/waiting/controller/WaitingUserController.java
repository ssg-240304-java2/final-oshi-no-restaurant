package kr.oshino.eataku.waiting.controller;

import kr.oshino.eataku.waiting.model.ListItem;
import kr.oshino.eataku.waiting.model.Restaurant;
import kr.oshino.eataku.waiting.model.UserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class WaitingUserController {

    @GetMapping("/waitingForm")
    public String waitingFormPage() {
        return "pages/waitingFormPage";
    }

    @GetMapping("/map")
    public String mapSearchPage() {
        return "pages/mapSearchPage";
    }

    @GetMapping("/jjFriends")
    public String jjFriendsPage() {
        return "pages/jjFriendPage";
    }

    @GetMapping("/card")
    public String cardSearchPage() {
        return "pages/cardSearchPage";
    }
    @GetMapping("/search")
    public String searchRestaurants(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "categories", required = false) List<String> categories,
            Model model) {

        List<Restaurant> allRestaurants = new ArrayList<>();

        // Define categories and addresses for dummy data
        String[] categoriesList = {"한식", "양식", "중식", "일식", "고깃집", "면요리", "분식", "태국", "베트남", "이탈리안"};
        String[] addresses = {
                "서울시 강남구", "서울시 서초구", "서울시 송파구",
                "서울시 마포구", "서울시 종로구", "서울시 영등포구",
                "서울시 동작구", "서울시 동대문구", "서울시 성동구", "서울시 중구"
        };

        // Generate dummy data for each category
        for (String category : categoriesList) {
            for (int i = 1; i <= 10; i++) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(category + " 가게 " + i);
                restaurant.setRating(4.5);
                restaurant.setCategory(category);
                restaurant.setAddress(addresses[i % addresses.length] + " " + i + "번지");
                restaurant.setImageUrl("https://via.placeholder.com/300x200"); // Placeholder image
                allRestaurants.add(restaurant);
            }
        }

        // Filter restaurants based on query and categories
        List<Restaurant> filteredRestaurants = allRestaurants.stream()
                .filter(restaurant -> {
                    boolean matchesQuery = (query == null || query.isEmpty()) || restaurant.getName().contains(query);
                    boolean matchesCategory = (categories == null || categories.isEmpty()) || categories.contains(restaurant.getCategory());
                    return matchesQuery && matchesCategory;
                })
                .collect(Collectors.toList());

        // Add filtered restaurants to the model
        model.addAttribute("restaurants", filteredRestaurants);

        return "pages/cardSearchPage";
    }

    @GetMapping("/profile/{userNo}")
    public String profilePage(@PathVariable("userNo") int userNo, Model model) {

        UserProfile userProfile = getUserProfile(userNo);

        // 조회한 정보를 모델에 추가
        model.addAttribute("publicListCount", userProfile.getPublicListCount());
        model.addAttribute("privateListCount", userProfile.getPrivateListCount());
        model.addAttribute("reviewCount", userProfile.getReviewCount());
        model.addAttribute("nickname", userProfile.getNickname());
        model.addAttribute("followingCount", userProfile.getFollowingCount());
        model.addAttribute("followerCount", userProfile.getFollowerCount());
        model.addAttribute("joinYear", userProfile.getJoinYear());
        model.addAttribute("introduction", userProfile.getIntroduction());
        model.addAttribute("weight", userProfile.getWeight());
        model.addAttribute("publicLists", userProfile.getPublicLists());
        model.addAttribute("privateLists", userProfile.getPrivateLists());

        return "/pages/userProfilePage";
    }

    private UserProfile getUserProfile(int userNo) {
        // 실제로는 데이터베이스에서 사용자 정보를 조회해야 함
        // 예제에서는 하드코딩된 데이터를 사용

        return new UserProfile(
                3, // 공개 리스트 수
                2, // 비공개 리스트 수
                3000, // 작성 리뷰 수
                "Sanghyeon Jwa", // 닉네임
                100, // 팔로잉 수
                "5K", // 팔로워 수
                2021, // 가입 연도
                "이곳은 소개글 작성 란 입니다.", // 소개 글
                "90KG", // 몸무게
                List.of( // 공개 리스트
                        new ListItem(1, "미슐랭 1스타 급 식당"),
                        new ListItem(2, "미슐랭 2스타 급 식당"),
                        new ListItem(3, "미슐랭 3스타 급 식당")
                ),
                List.of( // 비공개 리스트
                        new ListItem(4, "비공개 1스타 급 식당"),
                        new ListItem(5, "비공개 2스타 급 식당")
                )
        );
    }
}
