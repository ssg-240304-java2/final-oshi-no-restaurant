package kr.oshino.eataku.restaurant.admin.service;

import kr.oshino.eataku.restaurant.admin.entity.TemporarySave;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import kr.oshino.eataku.restaurant.admin.model.repository.TemporarySaveRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantAdminService {

    private final TemporarySaveRepository temporarySaveRepository;

    public void insertNewRestaurant(TemporarySaveDTO newRestaurant) {

        TemporarySave temporarySave = TemporarySave.builder()
                .companyNo(newRestaurant.getCompanyNo())
                .businessAddress(newRestaurant.getBusinessAddress())
                .companyName(newRestaurant.getCompanyName())
                .representativeName(newRestaurant.getRepresentativeName())
                .imgUrl(newRestaurant.getImgUrl())
                .build();

        temporarySaveRepository.save(temporarySave);

        log.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E temporarySave : {}", temporarySave);
    }
}
