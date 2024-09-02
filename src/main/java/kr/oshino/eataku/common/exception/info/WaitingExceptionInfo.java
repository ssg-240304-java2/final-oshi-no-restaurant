package kr.oshino.eataku.common.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WaitingExceptionInfo {

    DUPLICATED_WAITING(HttpStatus.BAD_REQUEST, 1300, "이미 해당 매장에 대기중 입니다!"),
    REQUEST_ALREADY_HANDLED(HttpStatus.BAD_REQUEST, 1301, "이미 입장 처리된 웨이팅입니다."),
    REQUEST_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, 1302, "이미 취소된 웨이팅 정보입니다."),
    WAITING_CLOSED(HttpStatus.BAD_REQUEST, 1303, "해당 매장의 웨이팅이 종료되었습니다."),
    RESTAURANT_CLOSED(HttpStatus.BAD_REQUEST, 1304, "현재 해당 매장은 영업중이 아닙니다."),
    NO_DATA_FOUND(HttpStatus.NOT_FOUND, 1305, "해당하는 웨이팅 정보가 없습니다!");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    WaitingExceptionInfo(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
