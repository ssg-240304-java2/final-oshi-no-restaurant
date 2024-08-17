package kr.oshino.eataku.restaurant.admin.model.dto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import kr.oshino.eataku.common.enums.FoodType;
import kr.oshino.eataku.common.enums.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterInfoDTO {

    private String restaurantName;
    private String contact;
    private String restaurantAddress;
    private Set<FoodType> foodType;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Set<HashTag> hashTag;
    private String description;
}
