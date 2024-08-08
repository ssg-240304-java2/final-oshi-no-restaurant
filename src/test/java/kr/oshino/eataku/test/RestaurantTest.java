package kr.oshino.eataku.test;

import kr.oshino.eataku.restaurant.admin.entity.RestaurantInfo;
import kr.oshino.eataku.restaurant.admin.model.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.util.Optional;

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
    void test1() {

        // given
//        RestaurantInfo restaurant = new RestaurantInfo(null,"test", "test", "test", Time.valueOf(LocalTime.now()), "test", "test", "test", 1.0, 1.0, HashTag.맛있어요, null, null, null, null, null);
        RestaurantInfo restaurant = new RestaurantInfo(
//                null,                             // id
//                "Sushi Place",                    // name
//                "Best sushi in town",             // description
//                "123 Sushi St",                   // address
//                Time.valueOf("09:00:00"),         // openingTime
//                "123-456-7890",                   // phoneNumber
//                "http://sushiplace.com",          // website
//                "info@sushiplace.com",            // email
//                35.6895,                          // latitude
//                139.6917,                         // longitude
//                HashTag.맛있어요,             // hashTag
//                null                             // extraField1
////                null,                             // extraField2
////                null,                             // extraField3
////                null,                             // extraField4
////                null                              // extraField5
        );
        restaurantRepository.save(restaurant);

        // when
        Optional<RestaurantInfo> restaurantInfo = restaurantRepository.findById(restaurant.getRestaurantNo());

        // then
        assertThat(restaurantInfo.isPresent());
        assertThat(restaurantInfo.get().getRestaurantNo()).isEqualTo(restaurant.getRestaurantNo());
        assertThat(restaurantInfo.get().getRestaurantName()).isEqualTo("Sushi Place");
        assertThat(restaurantInfo.get().getRestaurantAddress()).isEqualTo("123 Sushi St");
        assertThat(restaurantInfo.get().getBusinessHour()).isEqualTo(Time.valueOf("09:00:00"));
        assertThat(restaurantInfo.get().getContact()).isEqualTo("123-456-7890");
        assertThat(restaurantInfo.get().getPostNumber()).isEqualTo("http://sushiplace.com");
        assertThat(restaurantInfo.get().getImgUrl()).isEqualTo("info@sushiplace.com");
//        assertThat(restaurantInfo.get().getxCoordinate()).isEqualTo(35.6895);
//        assertThat(restaurantInfo.get().getyCoordinate()).isEqualTo(139.6917);
        assertThat(restaurantInfo.get().getHashtag()).isEqualTo("Sushi");
    }
}

