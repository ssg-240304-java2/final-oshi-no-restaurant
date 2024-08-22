package kr.oshino.eataku.reservation.user.model.dto.responseDto;


import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewWithMember {
    private Long memberNo;
    private int reviewStars;
}
