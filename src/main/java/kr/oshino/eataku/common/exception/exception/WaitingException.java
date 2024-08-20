package kr.oshino.eataku.common.exception.exception;

import kr.oshino.eataku.common.exception.info.WaitingExceptionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingException extends RuntimeException {

    private WaitingExceptionInfo info;
}
