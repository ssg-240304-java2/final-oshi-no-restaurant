package kr.oshino.eataku.reservation.user.model.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationUserResponseDto {
    public int httpCode;
    public String message;
    public String restaurantName;
}
