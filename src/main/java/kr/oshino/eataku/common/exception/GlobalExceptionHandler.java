package kr.oshino.eataku.common.exception;

import kr.oshino.eataku.common.exception.exception.ReservationException;
import kr.oshino.eataku.common.exception.exception.WaitingException;
import kr.oshino.eataku.common.response.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WaitingException.class)
    public ResponseEntity<BasicResponse<WaitingException>> waitingExceptionHandler(WaitingException exception) {
        return ResponseEntity.status(exception.getInfo().getHttpStatus())
                .body(BasicResponse.<WaitingException>builder()
                        .code(exception.getInfo().getCode())
                        .message(exception.getInfo().getMessage())
                        .build());
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<BasicResponse<ReservationException>> reservationExceptionHandler(ReservationException exception) {
        return ResponseEntity.status(exception.getInfo().getHttpStatus())
                .body(BasicResponse.<ReservationException>builder()
                        .code(exception.getInfo().getCode())
                        .message(exception.getInfo().getMessage())
                        .build());
    }
}
