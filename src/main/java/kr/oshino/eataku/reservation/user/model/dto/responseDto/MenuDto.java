package kr.oshino.eataku.reservation.user.model.dto.responseDto;


import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuDto {
    private String photoUrl;
    private String description;
    private String menuName;
}
