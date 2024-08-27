package kr.oshino.eataku.restaurant.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import kr.oshino.eataku.member.model.dto.CustomMemberDetails;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.*;
import kr.oshino.eataku.reservation.user.service.ReservationUserService;
import kr.oshino.eataku.restaurant.admin.model.dto.*;
import kr.oshino.eataku.restaurant.admin.service.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantAdminController {

    private final RestaurantAdminService restaurantAdminService;
    private final ReservationUserService reservationUserService;

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
     * @param newRestaurant
     * @param session
     * @return
     */
    @PostMapping("/infoRegister")
    public ResponseEntity<String> infoRegister(@RequestPart("newRestaurant") RestaurantInfoDTO newRestaurant,
                                               @RequestPart(value = "file", required = false) MultipartFile file,
                                               HttpSession session) {

        String account = (String) session.getAttribute("id");
        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E newInfo : {}, account : {}, file: {}", newRestaurant, account, file);

        restaurantAdminService.insertNewInfo(newRestaurant, session, file);

        return ResponseEntity.ok("/restaurant/main");

    }

    /***
     * 등록된 식당 정보 페이지 조회
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
    public String infoUpdate(@RequestPart("updateInfo") RestaurantInfoDTO updateInfo,
                             @RequestPart(value = "file", required = false) MultipartFile file) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E updateInfo : {}, file: {}", updateInfo, file);

        updateInfo.setRestaurantNo(loginedRestaurantNo);
        restaurantAdminService.updateRestaurant(updateInfo, file);

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
        return "restaurant/main";
    }

    /***
     * 날짜 클릭 시 등록된 웨이팅 세팅 조회
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

    /***
     * 웨이팅 데이터 삭제
     * @param waitingDate
     * @return
     */
    @DeleteMapping("/deleteWaitingSetting/{waitingDate}")
    public ResponseEntity<String> deleteWaitingSetting(@PathVariable String waitingDate) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("delete for date : {}", waitingDate);

        restaurantAdminService.deleteWaitingSetting(Date.valueOf(waitingDate), loginedRestaurantNo);

        return ResponseEntity.ok("삭제되었습니다.");
    }

    /***
     * 메뉴 등록
     * @param newMenu
     * @param file
     * @return
     */
    @PostMapping("/menuRegister")
    @ResponseBody
    public ResponseEntity<String> menuRegister(@RequestPart("newMenu") MenuDTO newMenu,
                                @RequestPart(value = "file", required = false) MultipartFile file){

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        log.info("\uD83C\uDF4E newMenu: {} and file: {}", newMenu, file);

        newMenu.setRestaurantNo(loginedRestaurantNo);
        restaurantAdminService.insertNewMenu(newMenu, file, loginedRestaurantNo);

        return ResponseEntity.ok("/restaurant/menu");
    }

    /***
     * 메뉴 조회
     * @param model
     * @return
     */
    @GetMapping("/menu")
    public String selectMenu(Model model) {

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        List<MenuDTO> registeredMenu = restaurantAdminService.selectAllMenus(loginedRestaurantNo);

        model.addAttribute("registeredMenu", registeredMenu);

        log.info("\uD83C\uDF4E cont menu: {}", registeredMenu);

        return "restaurant/menu";
    }

    /***
     * 메뉴 수정
     * @param updatedMenu
     * @param file
     * @return
     */
    @PostMapping("/updateMenu")
    public ResponseEntity<String> updateMenu(
            @RequestPart("updatedMenu") MenuDTO updatedMenu,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        restaurantAdminService.updateMenu(updatedMenu, file);

        return ResponseEntity.ok("메뉴가 성공적으로 수정되었습니다.");
    }

    // ToDo : 메뉴 수정, 삭제 해야 함 (수정은 컨트롤러만 개발됨)

    /***
     * 식당 프리뷰
     * @param model
     * @return
     */
    @GetMapping("/preview")
    public String preview(Model model){

        CustomMemberDetails member = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginedRestaurantNo = member.getRestaurantNo();

        // 식당 상세 정보
        RestaurantInfoDetails restaurant = reservationUserService.getRestaurantDetailsByReservation(loginedRestaurantNo);
        model.addAttribute("restaurant", restaurant);

        // 식당 메뉴 정보
        List<MenuDto> menu = reservationUserService.getMenu(loginedRestaurantNo);
        model.addAttribute("menu", menu);

        // 리뷰 상세 정보
        List<ReviewDetails> reviewDetails = reservationUserService.getReviewDetails(loginedRestaurantNo);
        model.addAttribute("ReviewDetails", reviewDetails);

        // 태그 횟수 정보
        List<String> tagCount = reservationUserService.getCountTags(loginedRestaurantNo);
        Map<String, Integer> tagCountMap = new LinkedHashMap<>();
        System.out.println("tagCountMap = " + tagCountMap);

        // 식당 지도 위치 정보
        List<MapDto> position = reservationUserService.getMapLocation(loginedRestaurantNo);

        // 식당 리뷰 사진
        List<ReviewImgDto> reviewImgDto = reservationUserService.getImg(loginedRestaurantNo);
        model.addAttribute("reviewImgDto", reviewImgDto);

        for (String tagCounts : tagCount) {
            String[] parts = tagCounts.split(",");
            if (parts.length == 2) {
                String tag = parts[0].trim();  // 태그 부분
                Integer count = Integer.parseInt(parts[1].trim());  // 횟수 부분
                tagCountMap.put(tag, count);
            } else {
                // 태그나 횟수가 올바른 형식이 아닐 경우를 처리
                System.err.println(" 올바른 형식이이 아님 " + tagCount);
            }
        }
        System.out.println("menu = " + menu);

        model.addAttribute("tagCountMap", tagCountMap);
        model.addAttribute("position",  position);

        System.out.println("reviewDetails = " + reviewDetails);
        System.out.println("restaurant = " + restaurant);
        System.out.println("tagCountMap = " + tagCountMap);
        System.out.println("tagCountMap = " + tagCountMap);
        System.out.println("position = " + position);
        System.out.println("reviewImgDto = " + reviewImgDto);

        return "restaurant/preview";
    }

}
