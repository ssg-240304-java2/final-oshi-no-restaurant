package kr.oshino.eataku.restaurant.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SalesDTO {

    private LocalDate salesDate;
    private int numberOfPeople;
    private LocalTime usageTime;
    private String usageType;

    private String customerName;
    private String customerContact;
    private int usageCount;
}
