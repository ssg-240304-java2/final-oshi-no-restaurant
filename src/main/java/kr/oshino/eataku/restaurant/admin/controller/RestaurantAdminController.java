package kr.oshino.eataku.restaurant.admin.controller;

import kr.oshino.eataku.restaurant.admin.model.dto.RegisterInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
    public String businessRegister(@RequestBody TemporarySaveDTO newRestaurant){

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newRestaurant : {} ", newRestaurant);

        restaurantAdminService.insertNewCertification(newRestaurant);

        return "redirect:/restaurant/certification";
    }

    /***
     * 식당 정보 등록
     * @return
     */
    @GetMapping("/infoRegister")
    public void info() {}

    @PostMapping("/infoRegister")
    public String infoRegister(@RequestBody RegisterInfoDTO newInfo){

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newInfo : {}", newInfo);

        restaurantAdminService.insertNewInfo(newInfo);

        return "redirect:/restaurant/infoRegister";

    }
}
