package kr.oshino.eataku.waiting.model.dto.responseDto;

import kr.oshino.eataku.common.enums.StatusType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReadWaitingResponseDto {

    private Long waitingNo;
    private int partySize;
    private StatusType waitingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Member Entity 정보
    private String name;
    private String nickname;
    private String phone;

    // Restaurant_Info Entity 정보
    private String restaurantName;
}
