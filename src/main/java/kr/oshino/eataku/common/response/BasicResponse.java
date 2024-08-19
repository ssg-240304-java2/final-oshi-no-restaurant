package kr.oshino.eataku.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicResponse<T> {

    @Builder.Default
    private Integer code = 200;

    private String message;

    private T data;
}
