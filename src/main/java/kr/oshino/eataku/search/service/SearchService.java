package kr.oshino.eataku.search.service;

import kr.oshino.eataku.member.model.repository.MemberRepository;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import kr.oshino.eataku.search.model.dto.SearchResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    public List<SearchResultDTO> selectQueryByKeyword(String keyword, int page, int size ) {
        Pageable pageable = PageRequest.of(page, size);
        List<Object[]> results = restaurantRepository.findAllByKeyword(keyword, pageable);

        return results.stream().map(this::mapToSearchResultDTO).collect(Collectors.toList());
    }

    private SearchResultDTO mapToSearchResultDTO(Object[] result) {
//        log.info("🩻🩻 [ SearchService ] mapToSearchResultDTO : {} ", Arrays.toString(result));
        Long restaurantNo = ((Number) result[0]).longValue();  // restaurant_no
        String restaurantName = (String) result[1];            // restaurant_name
        String restaurantAddress = (String) result[2];         // restaurant_address
        Double xCoordinate = 0d;
        Double yCoordinate = 0d;
        if (result[3] != null || result[4] != null) {
            xCoordinate = ((Number) result[3]).doubleValue(); // x_coordinate
            yCoordinate = ((Number) result[4]).doubleValue(); // y_coordinate
        }
        String imgUrl = (String) result[5];                    // img_url
        String foodType = (String) result[6];  // 해시태그 문자열
        String hashTags = (String) result[7];  // 카테고리 문자열
        Double rating = 0d;
        if(result[8] != null) {
            rating = ((Number) result[8]).doubleValue();
        }


        return new SearchResultDTO(restaurantNo, restaurantName, restaurantAddress,
                                   xCoordinate, yCoordinate, imgUrl, foodType, hashTags, rating);
    }

    public List<SearchResultDTO> selectQueryBylatitudeAndlongitude(Double latitude, Double longitude, String keyword) {
        List<Object[]> results = restaurantRepository.selectQueryBylatitudeAndlongitude(latitude, longitude, keyword);

        return results.stream().map(this::mapToSearchResultDTO).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void restaurantStat(){

        log.info("⏳⏳ [ SearchService ] Scheduler  \uFE0F\uFE0F");
        memberRepository.truncateBusiestRestaurant();
        memberRepository.updateBusiestRestaurant();

        memberRepository.truncateListAddRestaurant();
        memberRepository.updateListAddRestaurant();

        memberRepository.truncateDirectRestaurant();
        memberRepository.updateDirectRestaurant();
    }

    public List<SearchResultDTO> selectPopularLists() {

        List<Long> popularList = memberRepository.selectPopularRestaurants();
        List<SearchResultDTO> entityList = new ArrayList<>();
        if (!popularList.isEmpty()) {
            entityList = restaurantRepository.findPopularRestaurantsWithRatings(popularList);
        }
        return entityList;
    }

    public List<SearchResultDTO> selectBusiestLists() {

        List<Long> busiestList = memberRepository.selectBusiestRestaurants();
        List<SearchResultDTO> entityList = new ArrayList<>();
        if (!busiestList.isEmpty()) {
            entityList = restaurantRepository.findPopularRestaurantsWithRatings(busiestList);
        }
        return entityList;
    }

    public List<SearchResultDTO> selectDirectLists() {

        List<Long> directList = memberRepository.selectDirectRestaurants();
        List<SearchResultDTO> entityList = new ArrayList<>();
        if (!directList.isEmpty()) {
            entityList = restaurantRepository.findPopularRestaurantsWithRatings(directList);
        }
        return entityList;
    }
}
