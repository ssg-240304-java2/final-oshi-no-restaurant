package kr.oshino.eataku.waiting.model.dto.requestDto;

import kr.oshino.eataku.common.enums.StatusType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReadWaitingRequestDto {

    private Long memberNo;
    private Integer restaurantNo;
    private StatusType waitingStatus;       // 예약 전체 정보 or 대기중 정보 분리
}
