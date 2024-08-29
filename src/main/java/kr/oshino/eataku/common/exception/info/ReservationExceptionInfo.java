package kr.oshino.eataku.common.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationExceptionInfo {

    DUPLICATED_RESERVATION(HttpStatus.BAD_REQUEST, 1200, "이미 예약 중인 매장입니다!"),
    NO_SEATS_AVAILABLE(HttpStatus.BAD_REQUEST,1201 , "요청하신 인원 수에 맞는 자리가 부족합니다!");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    ReservationExceptionInfo(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
