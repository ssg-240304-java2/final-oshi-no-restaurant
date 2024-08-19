package kr.oshino.eataku.restaurant.admin.controller;

import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.dto.ReservSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

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
    public void businessView() {
    }

    @PostMapping("/certification")
    public ResponseEntity<String> businessRegister(@RequestBody TemporarySaveDTO newRestaurant, HttpSession session) {

        newRestaurant.setAccount((String) session.getAttribute("id"));

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newRestaurant : {} ", newRestaurant);

        restaurantAdminService.insertNewCertification(newRestaurant);

        return ResponseEntity.ok("/restaurant/infoRegister");
    }

    /***
     * 식당 정보 등록
     * @return
     */
    @GetMapping("/infoRegister")
    public void info() {
    }

    @PostMapping("/infoRegister")
    public ResponseEntity<String> infoRegister(@RequestBody RestaurantInfoDTO newInfo, HttpSession session) {

        String account = (String) session.getAttribute("id");
        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newInfo : {}, account : {}", newInfo, account);

        restaurantAdminService.insertNewInfo(newInfo, session);

        return ResponseEntity.ok("/restaurant/main");

    }

    /***
     * 식당 정보 수정
     * @param restaurantNo
     * @param model
     * @return
     */
    @GetMapping("/infoUpdate/{restaurantNo}")
    public String infoView(@PathVariable("restaurantNo") Long restaurantNo, Model model) {

        RestaurantInfoDTO restaurant = restaurantAdminService.selectMyRestaurant(14785L);

        List<ReservSettingDTO> reservSettings = restaurantAdminService.selectReservSetting(14785L);


        Set<FoodType> foodTypes = restaurant.getFoodTypes();
        Set<HashTag> hashTags = restaurant.getHashTags();
//        String imageData = restaurantAdminService.getRestaurantImageById(restaurantNo);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("foodTypes", foodTypes);
        model.addAttribute("hashTags", hashTags);
        model.addAttribute("reservSetting", reservSettings);
//        model.addAttribute("imageData", imageData);

        log.info("\uD83C\uDF4E foodTypes : {} ", foodTypes);
        log.info("\uD83C\uDF4E hashTags : {} ", hashTags);
        log.info("\uD83C\uDF4E reservSetting : {} ", reservSettings);

        return "restaurant/infoUpdate";
    }

    @PostMapping("/infoUpdate")
    public String infoUpdate(@RequestBody RestaurantInfoDTO updateInfo
//                             @RequestParam("storeImage")MultipartFile imageFile
    ) {

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E updateInfo : {}", updateInfo);

//        if(!imageFile.isEmpty()){
//            try{
//                String uploadDir = "/uploads/images/";
//                String fileName = imageFile.getOriginalFilename();
//                Path filePath = Paths.get(uploadDir + fileName);
//
//                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                String imgUrl = uploadDir + fileName;
//                updateInfo.setImgUrl(imgUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//                log.error("이미지 업로드 실패 : {}", e.getMessage());
//
//                return "redirect:/restaurant/infoUpdate/" + updateInfo.getRestaurantNo() + "?error";
//            }
//        }

        updateInfo.setRestaurantNo(14785L);
        restaurantAdminService.updateRestaurant(updateInfo);

        return "redirect:/restaurant/infoUpdate/"+ updateInfo.getRestaurantNo();
    }


    @PostMapping("/reservationInsert")
    public String setRegister(@RequestBody ReservSettingDTO reservSetting) {

        log.info("\uD83C\uDF4E reservation : {} ", reservSetting);
        return "redirect:/restaurant/reservationInsert/" + reservSetting.getReservationNo();
    }

}
