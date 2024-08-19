package kr.oshino.eataku.common.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WaitingExceptionInfo {

    DUPLICATED_WAITING(HttpStatus.BAD_REQUEST, 1300, "이미 해당 매장에 대기중 입니다!"),
    REQUEST_ALREADY_HANDLED(HttpStatus.BAD_REQUEST, 1301, "이미 입장 처리된 웨이팅입니다."),
    REQUEST_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, 1302, "이미 취소된 웨이팅 정보입니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    WaitingExceptionInfo(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
