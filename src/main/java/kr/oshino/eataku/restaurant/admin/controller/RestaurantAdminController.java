package kr.oshino.eataku.restaurant.admin.controller;

import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantAdminController {

    private final RestaurantAdminService restaurantAdminService;

    /***
     * 사업자 등록증 등록
     */
    @GetMapping("/certification")
    public void business(){}

    @PostMapping("/certification")
    public ResponseEntity<String> businessRegister(@RequestBody TemporarySaveDTO newRestaurant, HttpSession session){

        newRestaurant.setAccount((String)session.getAttribute("id"));

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newRestaurant : {} ", newRestaurant);

        restaurantAdminService.insertNewCertification(newRestaurant);

        return ResponseEntity.ok("/restaurant/infoRegister");
    }

    /***
     * 식당 정보 등록
     * @return
     */
    @GetMapping("/infoRegister")
    public void register() {}

    @PostMapping("/infoRegister")
    public ResponseEntity<String> infoRegister(@RequestBody RestaurantInfoDTO newInfo, HttpSession session){

        String account = (String)session.getAttribute("id");
        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newInfo : {}, account : {}", newInfo, account);

        restaurantAdminService.insertNewInfo(newInfo, session);

        return ResponseEntity.ok("/restaurant/main");

    }

    @GetMapping("/infoUpdate/{restaurantNo}")
    public String restaurantInfo(@PathVariable("restaurantNo") Long restaurantNo, Model model){

         RestaurantInfoDTO restaurant = restaurantAdminService.selectMyRestaurant(14779L);
         model.addAttribute("restaurant", restaurant);
         return "restaurant/infoUpdate";
    }

    @PostMapping("/infoUpdate/{restaurantNo}")
    public String infoUpdate(@RequestBody RestaurantInfoDTO updateInfo){

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E updateInfo : {}", updateInfo);

        updateInfo.setRestaurantNo(14779L);
        restaurantAdminService.updateRestaurant(updateInfo);

        return "redirect:/restaurant/infoUpdate";
    }
}
