package kr.oshino.eataku.restaurant.admin.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.dto.ReservSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.WaitingSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
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
    private final RestaurantRepository restaurantRepository;

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
        WaitingSettingDTO waitingSettings = restaurantAdminService.selectWaitingSetting(loginedRestaurantNo);

        Set<FoodType> foodTypes = restaurant.getFoodTypes();
        Set<HashTag> hashTags = restaurant.getHashTags();
//        String imageData = restaurantAdminService.getRestaurantImageById(restaurantNo);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("foodTypes", foodTypes);
        model.addAttribute("hashTags", hashTags);
        model.addAttribute("reservSetting", reservSettings);
        model.addAttribute("waitingSettings", waitingSettings);
//        model.addAttribute("imageData", imageData);

        log.info("\uD83C\uDF4E foodTypes : {} ", foodTypes);
        log.info("\uD83C\uDF4E hashTags : {} ", hashTags);
        log.info("\uD83C\uDF4E reservSetting : {} ", reservSettings);
        log.info("\uD83C\uDF4E waitingSetting : {}", waitingSettings);

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
     * @param newSetting
     * @return
     */
    @PostMapping("/reservationSetting")
    @ResponseBody
    public ReservSettingDTO reservationRegister(@RequestBody ReservSettingDTO newSetting) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E reservation : {} ", newSetting);

        newSetting.setRestaurantNo(loginedRestaurantNo);

        newSetting = restaurantAdminService.insertNewReserv(newSetting, loginedRestaurantNo);

        return newSetting;
    }

    /***
     * 등록된 예약 세팅 조회
     * @param reservationDate
     * @return
     */
    @GetMapping("/reservationSetting/{reservationDate}")
    public ResponseEntity<List<ReservSettingDTO>> selectReservationByDate(@PathVariable Date reservationDate) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        List<ReservSettingDTO> reservations = restaurantAdminService.findReservSettingByDate(reservationDate, loginedRestaurantNo);

        return ResponseEntity.ok(reservations);
    }

    /***
     * 예약 세팅 삭제
     * @param reservationNo
     * @return
     */
    @DeleteMapping("/deleteReservationSetting/{reservationNo}")
    public ResponseEntity<String> deleteReservationSetting(@PathVariable Long reservationNo){

        restaurantAdminService.deleteSetting(reservationNo);

        return ResponseEntity.ok("삭제되었습니다.");
    }

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

    @GetMapping("/waitingSetting")
    public ResponseEntity<WaitingSettingDTO> selectWaitingByDate(@PathVariable Date waitingDate){

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        WaitingSettingDTO waitings = restaurantAdminService.findWaitingSettingByDate(waitingDate, loginedRestaurantNo);

        return ResponseEntity.ok(waitings);
    }

    @PostMapping("/waitingSetting")
    public WaitingSettingDTO waitingRegister(@RequestBody WaitingSettingDTO newSetting){

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E newSetting : {}", newSetting);

        return newSetting;
    }

}
