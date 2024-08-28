package kr.oshino.eataku.restaurant.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesDTO {

    private Long ServiceNo;
    private String ServiceType;
    private int partySize;
    private LocalDate date;
    private LocalTime time;
    private String name;
    private String contact;
    private int count;
}
