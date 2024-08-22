package kr.oshino.eataku.restaurant.admin.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.restaurant.admin.entity.ReservationSetting;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.entity.WaitingSetting;
import kr.oshino.eataku.restaurant.admin.model.dto.ReservSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.WaitingSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.WaitingSettingRepository;
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
     * 사업자 등록증 페이지 조회
     */
    @GetMapping("/certification")
    public void businessView() {
    }

    /***
     * 사업자 등록증 등록
     * @param jsonData
     * @param session
     * @return
     */
    @PostMapping("/certification")
    public ResponseEntity<String> businessRegister(@RequestPart(value = "file", required = false) MultipartFile file,
                                                   @RequestPart("jsonData") TemporarySaveDTO jsonData,
                                                   HttpSession session) {

        jsonData.setAccount((String) session.getAttribute("id"));

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E file : {} and newRestaurant : {} ", file.isEmpty(), jsonData);

        restaurantAdminService.insertNewCertification(jsonData, file);

        return ResponseEntity.ok("/restaurant/infoRegister");
    }

    /***
     * 식당 정보 등록 페이지 조회
     */
    @GetMapping("/infoRegister")
    public void info() {
    }

    /***
     * 회원가입 시 식당 정보 등록
     * @param newInfo
     * @param session
     * @return
     */
    @PostMapping("/infoRegister")
    public ResponseEntity<String> infoRegister(@RequestPart("newInfo") RestaurantInfoDTO newInfo,
                                               @RequestPart(value = "file", required = false) MultipartFile file,
                                               HttpSession session) {

        String account = (String) session.getAttribute("id");
        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newInfo : {}, account : {}, file: {}", newInfo, account, file);

        restaurantAdminService.insertNewInfo(newInfo, session, file);

        return ResponseEntity.ok("/restaurant/main");

    }

    /***
     * 식당 정보 페이지 조회
     * @param model
     * @return
     */
    @GetMapping("/infoUpdate")
    public String infoView(Model model) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        RestaurantInfoDTO restaurant = restaurantAdminService.selectMyRestaurant(loginedRestaurantNo);      // 식당 정보 조회
        List<ReservSettingDTO> reservSettings = restaurantAdminService.selectReservSetting(loginedRestaurantNo);            // 예약 페이지 조회
        WaitingSettingDTO waitingSettings = restaurantAdminService.selectWaitingSetting(loginedRestaurantNo);       // 웨이팅 페이지 조회

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
    public String infoUpdate(@RequestBody RestaurantInfoDTO updateInfo) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E updateInfo : {}", updateInfo);

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

    /***
     * 등록된 웨이팅 세팅 조회
     * @param waitingDate
     * @return
     */
    @GetMapping("/waitingSetting/{waitingDate}")
    public ResponseEntity<WaitingSettingDTO> selectWaitingByDate(@PathVariable String waitingDate){
        log.info("🍎waitingDate: {}", waitingDate);

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        WaitingSettingDTO waitingSettings = restaurantAdminService.findWaitingSettingByDate(Date.valueOf(waitingDate), loginedRestaurantNo);

        log.info("🍎waitingSettings : {}", waitingSettings);

        if(waitingSettings != null) {
            return ResponseEntity.ok(waitingSettings);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /***
     *  웨이팅 세팅 등록
     * @param newSetting
     * @return
     */
    @PostMapping("/waitingSetting")
    @ResponseBody
    public WaitingSettingDTO waitingRegister(@RequestBody WaitingSettingDTO newSetting){

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E newSetting : {}", newSetting);

        newSetting.setRestaurantNo(loginedRestaurantNo);
        newSetting = restaurantAdminService.insertNewWaiting(newSetting, loginedRestaurantNo);

        return newSetting;
    }

    /***
     * 웨이팅 세팅 수정
     * @param updateSetting
     * @return
     */
    @PostMapping("/waitingUpdate")
    public String waitingUpdate(@RequestBody WaitingSettingDTO updateSetting) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E updateSetting: {}", updateSetting);

        updateSetting.setRestaurantNo(loginedRestaurantNo);

        if("N".equals(updateSetting.getWaitingStatus())){
            restaurantAdminService.deleteWaitingByDateAndRestaurantNo(updateSetting.getWaitingDate(), loginedRestaurantNo);
        } else {
            restaurantAdminService.updateWaiting(updateSetting, loginedRestaurantNo);
        }

        return "redirect:/restaurant/infoUpdate";
    }

    @DeleteMapping("/deleteWaitingSetting/{waitingDate}")
    public ResponseEntity<String> deleteWaitingSetting(@PathVariable String waitingDate) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("delete for date : {}", waitingDate);

        restaurantAdminService.deleteWaitingSetting(Date.valueOf(waitingDate), loginedRestaurantNo);

        return ResponseEntity.ok("삭제되었습니다.");
    }
}
