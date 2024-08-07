package kr.oshino.eataku.restaurant.admin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hashTag")
public class HashTag{       // 해시태그

    @EmbeddedId
    private Restaurant restaurantNo;        // 식당 고유 번호(fk)

    @EmbeddedId
    private HashtagList hashTagNo;      // 해시태그 번호(fk)
}
