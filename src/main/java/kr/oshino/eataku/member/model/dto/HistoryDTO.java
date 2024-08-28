package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor

public class HistoryDTO {

    private String restaurantName;
    private Long restaurantNo;
    private String imgUrl;
    private String restaurantAddress;
    private String updateAt;
    private String serviceType;
    private Long serviceNo;
    private String status;

    public HistoryDTO(String restaurantName, Long restaurantNo, String imgUrl, String restaurantAddress, Timestamp updateAt, String serviceType, Long serviceNo, String status) {
        this.restaurantName = restaurantName;
        this.restaurantNo = restaurantNo;
        this.imgUrl = imgUrl;
        this.restaurantAddress = restaurantAddress;
        this.updateAt = updateAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("M월 d일 HH:mm"));
        this.serviceType = serviceType;
        this.serviceNo = serviceNo;
        this.status = status;
    }
}
