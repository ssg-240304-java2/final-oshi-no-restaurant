package kr.oshino.eataku.reservation.user.model.dto.responseDto;

import kr.oshino.eataku.common.enums.Scope;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDetails {
    private String name;
    private String reviewContent;
    private String imgURL;
    private Scope scope;
    private LocalDateTime reviewDate;
}
