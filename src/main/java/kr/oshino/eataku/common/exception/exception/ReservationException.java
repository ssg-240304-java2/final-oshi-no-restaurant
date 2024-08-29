package kr.oshino.eataku.common.exception.exception;

import kr.oshino.eataku.common.exception.info.ReservationExceptionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationException extends RuntimeException {

    private ReservationExceptionInfo info;
}
