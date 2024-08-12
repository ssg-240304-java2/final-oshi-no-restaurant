package kr.oshino.eataku.test;

import kr.oshino.eataku.common.enums.HashTag;
import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Optional;

import static kr.oshino.eataku.common.enums.HashTag.미쉐린1스타;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RestaurantTest {

    @Autowired
    public RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("식당 테스트")
    void test2() {}


    @Test
    @DisplayName("식당 테스트")
    @Transactional
    @Rollback(false)
    void test1() {

        // given
        RestaurantInfo restaurant = new RestaurantInfo(
                null,                             // id
                "Sushi Place",                    // name
                "Best sushi in town",             // description
                "123 Sushi St",                   // address
                "sushi",      // category
                Time.valueOf("09:00:00"),       // openingTime
                Time.valueOf("10:00:00"),       // closingTime
                "123-456-7890",                   // contact
                "100000",
                "info@sushiplace.com",          // imgurl
                35.6895,                          // latitude
                139.6917,                         // longitude
                미쉐린1스타            // hashTag
        );
        restaurantRepository.save(restaurant);

        // when
        Optional<RestaurantInfo> restaurantInfo = restaurantRepository.findById(restaurant.getRestaurantNo());

        // then
        assertThat(restaurantInfo.isPresent()).isTrue();
        assertThat(restaurantInfo.get().getRestaurantNo()).isEqualTo(restaurant.getRestaurantNo());
        assertThat(restaurantInfo.get().getRestaurantName()).isEqualTo("Sushi Place");
        assertThat(restaurantInfo.get().getDescription()).isEqualTo("Best sushi in town");
        assertThat(restaurantInfo.get().getRestaurantAddress()).isEqualTo("123 Sushi St");
        assertThat(restaurantInfo.get().getFoodType()).isEqualTo("sushi");
        assertThat(restaurantInfo.get().getOpeningTime()).isEqualTo(Time.valueOf("09:00:00"));
        assertThat(restaurantInfo.get().getClosingTime()).isEqualTo(Time.valueOf("10:00:00"));
        assertThat(restaurantInfo.get().getContact()).isEqualTo("123-456-7890");
        assertThat(restaurantInfo.get().getPostNumber()).isEqualTo("100000");
        assertThat(restaurantInfo.get().getImgUrl()).isEqualTo("info@sushiplace.com");
        assertThat(restaurantInfo.get().getXCoordinate()).isEqualTo(35.6895);
        assertThat(restaurantInfo.get().getYCoordinate()).isEqualTo(139.6917);
        assertThat(restaurantInfo.get().getHashtag()).isEqualTo(미쉐린1스타);

    }
}

