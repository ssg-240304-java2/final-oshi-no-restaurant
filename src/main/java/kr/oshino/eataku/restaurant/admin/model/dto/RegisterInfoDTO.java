package kr.oshino.eataku.restaurant.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterInfoDTO {

    private String restaurantName;
    private String contact;
    private String restaurantAddress;
    private List<Enum> foodType;        // TODO
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<Enum> hashTag;         // TODO
    private String description;
}
