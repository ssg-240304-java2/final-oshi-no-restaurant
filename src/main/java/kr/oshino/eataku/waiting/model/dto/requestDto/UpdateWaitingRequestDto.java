package kr.oshino.eataku.waiting.model.dto.requestDto;

import jakarta.annotation.Nonnull;
import kr.oshino.eataku.common.enums.StatusType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateWaitingRequestDto {

    private Long waitingNo;
    private Long memberNo;
}
