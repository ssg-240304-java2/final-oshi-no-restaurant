package kr.oshino.eataku.restaurant.admin.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import kr.oshino.eataku.restaurant.admin.entity.AccountInfo;
import kr.oshino.eataku.restaurant.admin.entity.Certification;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.entity.TemporarySave;
import kr.oshino.eataku.restaurant.admin.model.dto.RestaurantInfoDTO;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.restaurant.admin.model.repository.TemporarySaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantAdminService {

    private final TemporarySaveRepository temporarySaveRepository;
    private final RestaurantRepository restaurantRepository;
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

            if(updateInfo.getImgUrl() != null){
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
}
