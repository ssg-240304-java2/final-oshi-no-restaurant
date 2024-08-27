package kr.oshino.eataku.restaurant.admin.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.common.util.FileUploadUtil;
import kr.oshino.eataku.reservation.user.entity.Reservation;
import kr.oshino.eataku.reservation.user.model.dto.responseDto.RestaurantInfoDetails;
import kr.oshino.eataku.restaurant.admin.entity.*;
import kr.oshino.eataku.restaurant.admin.model.dto.*;
import kr.oshino.eataku.restaurant.admin.model.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantAdminService {

    private final TemporarySaveRepository temporarySaveRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationSettingRepository reservationSettingRepository;
    private final WaitingSettingRepository waitingSettingRepository;
    private final MenuRepository menuRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    FileUploadUtil fileUploadUtil;

    /***
     * 사업자 등록 인증
     * @param jsonData
     */
    public void insertNewCertification(TemporarySaveDTO jsonData, MultipartFile file) {

        String uploadImgUrl = "";

        if (file != null && !file.isEmpty()) {
            try {
                uploadImgUrl = fileUploadUtil.uploadFile(file);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed. Please try again.", e);
            }
        }

        TemporarySave temporarySave = TemporarySave.builder()
                .companyNo(jsonData.getCompanyNo())
                .businessAddress(jsonData.getBusinessAddress())
                .companyName(jsonData.getCompanyName())
                .representativeName(jsonData.getRepresentativeName())
                .imgUrl(uploadImgUrl)
                .account(jsonData.getAccount())
                .build();

        temporarySaveRepository.save(temporarySave);


        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E temporarySave : {}", temporarySave);
    }

    /***
     * 식당 정보 등록
     * @param newInfo
     * @param session
     */
    public void insertNewInfo(RestaurantInfoDTO newInfo, HttpSession session, MultipartFile file) {

        String uploadImgUrl = "";

        if (file != null && !file.isEmpty()) {
            try {
                uploadImgUrl = fileUploadUtil.uploadFile(file);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed. Please try again.", e);
            }
        }

        TemporarySave certificationDTO = temporarySaveRepository.findByAccount((String) session.getAttribute("id"));

        // 사업자 등록증 등록
        Certification certification = Certification.builder()
                .businessAddress(certificationDTO.getBusinessAddress())
                .companyName(certificationDTO.getCompanyName())
                .representativeName(certificationDTO.getRepresentativeName())
                .imgUrl(uploadImgUrl)
                .companyNo(certificationDTO.getCompanyNo())
                .build();

        // 계정 등록
        AccountInfo accountInfo = AccountInfo.builder()
                .id((String) session.getAttribute("id"))
                .email((String) session.getAttribute("email"))
                .password(bCryptPasswordEncoder.encode((String) session.getAttribute("password")))
                .build();

        // 식당 정보 등록
        RestaurantInfo registerInfo = RestaurantInfo.builder()
                .restaurantName(newInfo.getRestaurantName())
                .contact(newInfo.getContact())
                .restaurantAddress(newInfo.getRestaurantAddress())
                .postCode(newInfo.getPostCode())
                .address(newInfo.getAddress())
                .detailAddress(newInfo.getDetailAddress())
                .extraAddress(newInfo.getExtraAddress())
                .foodTypes(newInfo.getFoodTypes())
                .openingTime(Time.valueOf(newInfo.getOpeningTime()))
                .closingTime(Time.valueOf(newInfo.getClosingTime()))
                .hashTags(newInfo.getHashTags())
                .description(newInfo.getDescription())
                .imgUrl(uploadImgUrl)
                .build();

        restaurantRepository.save(registerInfo);

        certification.setRestaurantNo(registerInfo);
        accountInfo.setRestaurantNo(registerInfo);

        registerInfo.setCertification(certification);
        registerInfo.setAccountInfo(accountInfo);

        restaurantRepository.save(registerInfo);

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E registerInfo : {}", registerInfo);
    }

    /***
     * 식당 정보 조회
     * @param restaurantNo
     * @return
     */
    public RestaurantInfoDTO selectMyRestaurant(Long restaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(restaurantNo).orElse(null);

        if (restaurantInfo != null) {
            RestaurantInfoDTO restaurant = new RestaurantInfoDTO();
            restaurant.setRestaurantNo(restaurantInfo.getRestaurantNo());
            restaurant.setRestaurantName(restaurantInfo.getRestaurantName());
            restaurant.setContact(restaurantInfo.getContact());
            restaurant.setFoodTypes(restaurantInfo.getFoodTypes());
            restaurant.setPostCode(restaurantInfo.getPostCode());
            restaurant.setAddress(restaurantInfo.getAddress());
            restaurant.setDetailAddress(restaurantInfo.getDetailAddress());
            restaurant.setExtraAddress(restaurantInfo.getExtraAddress());
            restaurant.setOpeningTime(restaurantInfo.getOpeningTime().toLocalTime());
            restaurant.setClosingTime(restaurantInfo.getClosingTime().toLocalTime());
            restaurant.setHashTags(restaurantInfo.getHashTags());
            restaurant.setDescription(restaurantInfo.getDescription());
            restaurant.setImgUrl(restaurantInfo.getImgUrl());

            log.info("🍎restaurant : {}", restaurant);
            return restaurant;
        } else {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantNo);
        }
    }

    /***
     * 식당 정보 수정
     * @param updateInfo
     */
    @Transactional
    public void updateRestaurant(RestaurantInfoDTO updateInfo, MultipartFile file) {

        Optional<RestaurantInfo> restaurantInfoOpt = restaurantRepository.findById(updateInfo.getRestaurantNo());

        String uploadImgUrl = "";

        if (file != null && !file.isEmpty()) {
            try {
                uploadImgUrl = fileUploadUtil.uploadFile(file);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed. Please try again.", e);
            }
        }

        if (restaurantInfoOpt.isPresent()) {
            RestaurantInfo restaurantInfo = restaurantInfoOpt.get();

            restaurantInfo.setRestaurantName(updateInfo.getRestaurantName());
            restaurantInfo.setContact(updateInfo.getContact());
            restaurantInfo.setPostCode(updateInfo.getPostCode());
            restaurantInfo.setAddress(updateInfo.getAddress());
            restaurantInfo.setDetailAddress(updateInfo.getDetailAddress());
            restaurantInfo.setExtraAddress(updateInfo.getExtraAddress());
            restaurantInfo.setOpeningTime(Time.valueOf(updateInfo.getOpeningTime()));
            restaurantInfo.setClosingTime(Time.valueOf(updateInfo.getClosingTime()));
            restaurantInfo.setHashTags(updateInfo.getHashTags());
            restaurantInfo.setDescription(updateInfo.getDescription());
            restaurantInfo.setImgUrl(uploadImgUrl);

            if (updateInfo.getImgUrl() != null) {
                restaurantInfo.setImgUrl(uploadImgUrl);
            }

            restaurantRepository.save(restaurantInfo);

            log.info("\uD83C\uDF4E restaurant : {} file: {}", restaurantInfo, file);
        } else {
            throw new EntityNotFoundException("Restaurant not found with id: " + updateInfo.getRestaurantNo());
        }
    }


//    public String getRestaurantImageById(Long restaurantNo) {
//
//        RestaurantInfo restaurantInfo = restaurantRepository.findById(restaurantNo).orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + restaurantNo));
//
//        return restaurantInfo.getImgUrl();
//    }

    // 예약 페이지 조회
    public List<ReservSettingDTO> selectReservSetting(Long restaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(restaurantNo).orElseThrow(() -> new EntityNotFoundException("Restaurant not found width id: " + restaurantNo));

        List<ReservationSetting> reservationSettings = reservationSettingRepository.findByRestaurantNo(restaurantInfo);

        return reservationSettings.stream().map(reservationSetting -> {
            ReservSettingDTO reservSetting = new ReservSettingDTO();
            reservSetting.setReservationNo(reservationSetting.getReservationNo());
            reservSetting.setReservationDate(reservationSetting.getReservationDate().toLocalDate());
            reservSetting.setReservationTime(reservationSetting.getReservationTime().toLocalTime());
            reservSetting.setReservationPeople(reservationSetting.getReservationPeople());
            return reservSetting;
        }).collect(Collectors.toList());
    }

    // 예약 정보 등록
    public ReservSettingDTO insertNewReserv(ReservSettingDTO newSetting, Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        ReservationSetting reservationSetting = ReservationSetting.builder()
                .reservationDate(Date.valueOf(newSetting.getReservationDate()))
                .reservationTime(Time.valueOf(newSetting.getReservationTime()))
                .reservationPeople(newSetting.getReservationPeople())
                .restaurantNo(restaurantInfo)
                .build();

        reservationSettingRepository.save(reservationSetting);

        log.info("\uD83C\uDF4E reservationSetting : {}", reservationSetting);

        newSetting.setReservationNo(reservationSetting.getReservationNo());
        return newSetting;
    }

    // 등록된 예약 내역 조회
    public List<ReservSettingDTO> findReservSettingByDate(Date reservationDate, Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        List<ReservationSetting> reservationSettings = reservationSettingRepository.findByReservationDateAndRestaurantNo(reservationDate, restaurantInfo);

        return reservationSettings.stream().map(reservationSetting -> {
            ReservSettingDTO reservSettingDTO = new ReservSettingDTO();
            reservSettingDTO.setRestaurantNo(loginedRestaurantNo);
            reservSettingDTO.setReservationNo(reservationSetting.getReservationNo());
            reservSettingDTO.setReservationDate(reservationSetting.getReservationDate().toLocalDate());
            reservSettingDTO.setReservationTime(reservationSetting.getReservationTime().toLocalTime());
            reservSettingDTO.setReservationPeople(reservationSetting.getReservationPeople());
            return reservSettingDTO;
        }).collect(Collectors.toList());

    }

    // 예약 삭제
    public void deleteSetting(Long reservationNo) {

        ReservationSetting reservationSetting = reservationSettingRepository.findById(reservationNo)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + reservationNo));

        reservationSettingRepository.delete(reservationSetting);

    }

    // 웨이팅 페이지 조회
    public WaitingSettingDTO selectWaitingSetting(Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo).orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        LocalDate today = LocalDate.now();
        WaitingSetting waitingSetting = waitingSettingRepository.findByWaitingDateAndRestaurantNo(Date.valueOf(today), restaurantInfo);

        if (waitingSetting != null) {
            return WaitingSettingDTO.builder()
                    .waitingNo(waitingSetting.getWaitingNo())
                    .restaurantNo(waitingSetting.getRestaurantNo().getRestaurantNo())
                    .waitingDate(waitingSetting.getWaitingDate().toLocalDate())
                    .startTime(waitingSetting.getStartTime().toLocalTime())
                    .endTime(waitingSetting.getEndTime().toLocalTime())
                    .waitingPeople(waitingSetting.getWaitingPeople())
                    .waitingStatus(waitingSetting.getWaitingStatus())
                    .build();
        } else {
            return null;
        }

    }

    // 등록된 웨이팅 내역 조회
    public WaitingSettingDTO findWaitingSettingByDate(Date waitingDate, Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo).orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        WaitingSetting waitingSettings = waitingSettingRepository.findByWaitingDateAndRestaurantNo(waitingDate, restaurantInfo);

        if (waitingSettings != null) {
            WaitingSettingDTO waitingSettingDTO = new WaitingSettingDTO();
            waitingSettingDTO.setRestaurantNo(loginedRestaurantNo);
            waitingSettingDTO.setWaitingNo(waitingSettings.getWaitingNo());
            waitingSettingDTO.setWaitingDate(waitingSettings.getWaitingDate().toLocalDate());
            waitingSettingDTO.setStartTime(waitingSettings.getStartTime().toLocalTime());
            waitingSettingDTO.setEndTime(waitingSettings.getEndTime().toLocalTime());
            waitingSettingDTO.setWaitingStatus(waitingSettings.getWaitingStatus());
            waitingSettingDTO.setWaitingPeople(waitingSettings.getWaitingPeople());
            return waitingSettingDTO;
        } else {
            return null;
        }
    }

    // 웨이팅 등록
    @Transactional
    public WaitingSettingDTO insertNewWaiting(WaitingSettingDTO newSetting, Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo).orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        WaitingSetting existingSetting = waitingSettingRepository.findByWaitingDateAndRestaurantNo(Date.valueOf(newSetting.getWaitingDate()), restaurantInfo);

        if (existingSetting != null) {
            waitingSettingRepository.delete(existingSetting);
        }

        WaitingSetting waitingSetting = WaitingSetting.builder()
                .restaurantNo(restaurantInfo)
                .waitingDate(Date.valueOf(newSetting.getWaitingDate()))
                .startTime(Time.valueOf(newSetting.getStartTime()))
                .endTime(Time.valueOf(newSetting.getEndTime()))
                .waitingStatus(newSetting.getWaitingStatus())
                .waitingPeople(newSetting.getWaitingPeople())
                .build();

        WaitingSetting savedSetting = waitingSettingRepository.save(waitingSetting);

        log.info("\uD83C\uDF4E waitingSetting: {}", waitingSetting);
        return convertToDTO(savedSetting);

    }

    private WaitingSettingDTO convertToDTO(WaitingSetting waitingSetting) {
        return WaitingSettingDTO.builder()
                .waitingNo(waitingSetting.getWaitingNo())
                .restaurantNo(waitingSetting.getRestaurantNo().getRestaurantNo())
                .waitingDate(waitingSetting.getWaitingDate().toLocalDate())
                .startTime(waitingSetting.getStartTime().toLocalTime())
                .endTime(waitingSetting.getEndTime().toLocalTime())
                .waitingPeople(waitingSetting.getWaitingPeople())
                .waitingStatus(waitingSetting.getWaitingStatus())
                .build();

    }

    // 웨이팅 수정
    @Transactional
    public void updateWaiting(WaitingSettingDTO updateSetting, Long loginedRestaurantNo) {

        WaitingSetting existingSetting = waitingSettingRepository.findById(updateSetting.getWaitingNo()).orElseThrow(() -> new EntityNotFoundException("WaitingSetting not found with id: " + loginedRestaurantNo));

        existingSetting.setWaitingDate(Date.valueOf(updateSetting.getWaitingDate()));
        existingSetting.setStartTime(Time.valueOf(updateSetting.getStartTime()));
        existingSetting.setEndTime(Time.valueOf(updateSetting.getEndTime()));
        existingSetting.setWaitingPeople(updateSetting.getWaitingPeople());
        existingSetting.setWaitingStatus(updateSetting.getWaitingStatus());

        waitingSettingRepository.save(existingSetting);
    }

    public void deleteWaitingByDateAndRestaurantNo(LocalDate waitingDate, Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo).orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        WaitingSetting existingSetting = waitingSettingRepository.findByWaitingDateAndRestaurantNo(Date.valueOf(waitingDate), restaurantInfo);

        if (existingSetting != null) {
            waitingSettingRepository.delete(existingSetting);
            log.info("\uD83C\uDF4E deleted setting for date : {} and restaurantNo : {}", waitingDate, loginedRestaurantNo);
        } else {
            log.warn("No waiting setting found to delete for date: {} and restaurantNo: {}", waitingDate, loginedRestaurantNo);
        }

    }

    @Transactional
    public void deleteWaitingSetting(Date waitingDate, Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo).orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        WaitingSetting waitingSetting = waitingSettingRepository.findByWaitingDateAndRestaurantNo(waitingDate, restaurantInfo);

        if (waitingSetting == null) {
            log.warn("No waiting settings found for date: {} and restaurant id: {}", waitingDate, loginedRestaurantNo);
            throw new EntityNotFoundException("No waiting settings found for the specified date and restaurant.");
        }

        waitingSettingRepository.delete(waitingSetting);
        log.info("\uD83C\uDF4E date: {} and restaurant: {}", waitingDate, loginedRestaurantNo);
    }

    /***
     * 메뉴 등록
     * @param newMenu
     * @param file
     * @param loginedRestaurantNo
     * @return
     */
    public MenuDTO insertNewMenu(MenuDTO newMenu, MultipartFile file, Long loginedRestaurantNo) {

        String uploadImgUrl = "";

        if (file != null && !file.isEmpty()) {
            try {
                uploadImgUrl = fileUploadUtil.uploadFile(file);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed. Please try again.", e);
            }
        }

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        Menu menu = Menu.builder()
                .menuName(newMenu.getMenuName())
                .description(newMenu.getDescription())
                .restaurantNo(restaurantInfo)
                .imgUrl(uploadImgUrl)
                .build();

        menuRepository.save(menu);

        log.info("\uD83C\uDF4E service menu: {}", menu);

        newMenu.setMenuNo(menu.getMenuNo());
        return newMenu;
    }

    // 등록된 메뉴 조회
    public List<MenuDTO> selectAllMenus(Long loginedRestaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(loginedRestaurantNo)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + loginedRestaurantNo));

        List<Menu> menus = menuRepository.findByRestaurantNo(restaurantInfo);

        return menus.stream().map(menu -> {
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setRestaurantNo(loginedRestaurantNo);
            menuDTO.setMenuName(menu.getMenuName());
            menuDTO.setDescription(menu.getDescription());
            menuDTO.setImgUrl(menu.getImgUrl());

            log.info("🍎service menuDTO: {}", menuDTO);
            return menuDTO;
        }).collect(Collectors.toList());
    }

    public void updateMenu(MenuDTO updatedMenu, MultipartFile file) {

    }
}


