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

import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantAdminService {

    private final TemporarySaveRepository temporarySaveRepository;
    private final RestaurantRepository restaurantRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public void insertNewInfo(RestaurantInfoDTO newInfo, HttpSession session) {

        TemporarySave certificationDTO = temporarySaveRepository.findByAccount((String)session.getAttribute("id"));

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
                .foodType(newInfo.getFoodType())
                .openingTime(Time.valueOf(newInfo.getOpeningTime()))
                .closingTime(Time.valueOf(newInfo.getClosingTime()))
                .hashTag(newInfo.getHashTag())
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

    public RestaurantInfoDTO selectMyRestaurant(Long restaurantNo) {

        RestaurantInfo restaurantInfo = restaurantRepository.findById(restaurantNo).orElse(null);

        if (restaurantInfo != null) {
            RestaurantInfoDTO restaurant = new RestaurantInfoDTO();
            restaurant.setRestaurantNo(restaurantInfo.getRestaurantNo());
            restaurant.setRestaurantName(restaurantInfo.getRestaurantName());
            restaurant.setContact(restaurantInfo.getContact());
            restaurant.setFoodType(restaurantInfo.getFoodType());
            restaurant.setPostCode(restaurantInfo.getPostCode());
            restaurant.setAddress(restaurantInfo.getAddress());
            restaurant.setDetailAddress(restaurantInfo.getDetailAddress());
            restaurant.setExtraAddress(restaurantInfo.getExtraAddress());
            restaurant.setOpeningTime(restaurantInfo.getOpeningTime().toLocalTime());
            restaurant.setClosingTime(restaurantInfo.getClosingTime().toLocalTime());
            restaurant.setHashTag(restaurantInfo.getHashTag());
            restaurant.setDescription(restaurantInfo.getDescription());
            return restaurant;
        } else {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantNo);
        }
    }

    public void updateRestaurant(RestaurantInfoDTO updateInfo) {



    }


}
