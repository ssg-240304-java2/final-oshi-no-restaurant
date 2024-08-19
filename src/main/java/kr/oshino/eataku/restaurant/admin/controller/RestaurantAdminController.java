package kr.oshino.eataku.restaurant.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.dto.ReservSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
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
     * 회원가입 시 식당 정보 등록
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
     * 식당 정보 조회
     * @param model
     * @return
     */
    @GetMapping("/infoUpdate")
    public String infoView(Model model) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        RestaurantInfoDTO restaurant = restaurantAdminService.selectMyRestaurant(loginedRestaurantNo);

        List<ReservSettingDTO> reservSettings = restaurantAdminService.selectReservSetting(loginedRestaurantNo);


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

    /***
     * 식당 정보 수정
     * @param updateInfo
     * @return
     */
    @PostMapping("/infoUpdate")
    public String infoUpdate(@RequestBody RestaurantInfoDTO updateInfo
//                             @RequestParam("storeImage")MultipartFile imageFile
    ) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

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

        updateInfo.setRestaurantNo(loginedRestaurantNo);
        restaurantAdminService.updateRestaurant(updateInfo);

        return "redirect:/restaurant/infoUpdate";
    }

    /***
     * 예약 세팅 등록
     * @param reservSetting
     * @return
     */
    @PostMapping("/reservationSetting")
    public String setRegister(@RequestBody ReservSettingDTO reservSetting) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E reservation : {} ", reservSetting);

        RestaurantInfo restaurantInfo = new RestaurantInfo();
        restaurantInfo.setRestaurantNo(loginedRestaurantNo);
        reservSetting.setRestaurantNo(restaurantInfo);

        restaurantAdminService.insertNewReserv(reservSetting);

        return "redirect:/restaurant/infoUpdate";
    }

    /***
     * 등록된 예약 조회
     * @param reservationDate
     * @param session
     * @return
     */
    @GetMapping("/reservationSetting/{reservationDate}")
    public ResponseEntity<List<ReservSettingDTO>> selectReservationByDate(@PathVariable Date reservationDate, HttpSession session) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        List<ReservSettingDTO> reservations = restaurantAdminService.findReservSettingByDate(reservationDate, loginedRestaurantNo);

        return ResponseEntity.ok(reservations);
    }

//    @PostMapping("/reservationSetting")
//    public String reservationUpdate(@RequestBody ReservSettingDTO updateSetting) {
//
//        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long loginedRestaurantNo = member.getRestaurantNo();
//
//        log.info("\uD83C\uDF4E controller : {}", updateSetting);
//
//        updateSetting.setRestaurantNo(loginedRestaurantNo);
//
//    }

    /***
     * 메인 페이지 조회
     * @param request
     * @return
     */
    @GetMapping("/main")
    public String main(HttpServletRequest request) {
        String method = request.getMethod();
        log.info("\uD83C\uDF4E main Request method : {} ", method);
        return "restaurant/main";
    }

}
