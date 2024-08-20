package kr.oshino.eataku.restaurant.admin.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.restaurant.admin.entity.*;
import kr.oshino.eataku.restaurant.admin.model.dto.ReservSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.WaitingSettingDTO;
import kr.oshino.eataku.restaurant.admin.model.repository.ReservationSettingRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.TemporarySaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /***
     * 사업자 등록 인증
     * @param newRestaurant
     */
    public void insertNewCertification(TemporarySaveDTO newRestaurant) {

        TemporarySave temporarySave = TemporarySave.builder()
                .companyNo(newRestaurant.getCompanyNo())
                .businessAddress(newRestaurant.getBusinessAddress())
                .companyName(newRestaurant.getCompanyName())
                .representativeName(newRestaurant.getRepresentativeName())
                .imgUrl(newRestaurant.getImgUrl())
                .account(newRestaurant.getAccount())
                .build();

        temporarySaveRepository.save(temporarySave);

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E temporarySave : {}", temporarySave);
    }

    /***
     * 식당 정보 등록
     * @param newInfo
     * @param session
     */
    public void insertNewInfo(RestaurantInfoDTO newInfo, HttpSession session) {

        TemporarySave certificationDTO = temporarySaveRepository.findByAccount((String) session.getAttribute("id"));

        Certification certification = Certification.builder()
                .businessAddress(certificationDTO.getBusinessAddress())
                .companyName(certificationDTO.getCompanyName())
                .representativeName(certificationDTO.getRepresentativeName())
                .imgUrl(certificationDTO.getImgUrl())
                .companyNo(certificationDTO.getCompanyNo())
                .build();
        // 여기까지 사업자등록증

        AccountInfo accountInfo = AccountInfo.builder()
                .id((String) session.getAttribute("id"))
                .email((String) session.getAttribute("email"))
                .password(bCryptPasswordEncoder.encode((String) session.getAttribute("password")))
                .build();

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
                // 여기까지 restaurantInfo
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
    public void updateRestaurant(RestaurantInfoDTO updateInfo) {

        Optional<RestaurantInfo> restaurantInfoOpt = restaurantRepository.findById(updateInfo.getRestaurantNo());

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

            if (updateInfo.getImgUrl() != null) {
                restaurantInfo.setImgUrl(updateInfo.getImgUrl());
            }

            restaurantRepository.save(restaurantInfo);

            log.info("\uD83C\uDF4E restaurant : {}", restaurantInfo);
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

    // 등록된 예약 세팅 조회
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

    // 등록된 예약 조회
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


    public WaitingSettingDTO selectWaitingSetting(Long loginedRestaurantNo) {

        WaitingSettingDTO waitingSetting = new WaitingSettingDTO();
        return waitingSetting;
    }
}
