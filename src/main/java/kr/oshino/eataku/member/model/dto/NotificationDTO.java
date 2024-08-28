package kr.oshino.eataku.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NotificationDTO {

    private String type;
    private String referenceName;
    private String date;
    private String img;
    private String message;
    private String serviceType;
    private Long serviceNo;

    public void setType(String type) {
        this.type = type;

        switch (type) {
            case "follow":
                this.img = "follow";
                break;
            case "list":
                this.img = "list";
                break;
            case "reservation":
            case "waiting":
                this.img = "check";
                break;
            case "reservationCancel":
            case "waitingCancel":
                this.img = "cancel";
                break;
        }
    }

    public void setDate(Timestamp date) {
        this.date = date.toLocalDateTime().format(DateTimeFormatter.ofPattern("M월 d일"));
    }
}
