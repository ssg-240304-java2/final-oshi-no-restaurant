package kr.oshino.eataku.restaurant.admin.controller;

import kr.oshino.eataku.restaurant.admin.model.dto.CertificationDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public void register(){}

    @PostMapping("/certification")
    public String registerRestaurant(@RequestBody TemporarySaveDTO newRestaurant){

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newRestaurant : {} ", newRestaurant);

        restaurantAdminService.insertNewRestaurant(newRestaurant);

        return "redirect:/restaurant/certification";
    }

    /***
     * 식당 정보 등록
     * @return
     */
    @GetMapping("/restaurantInfo")
    public String restaurantInfo() {

        return "restaurant/info";
    }
}
