package com.itoxi.petnuri.global.common.exception;

import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 찾을 수 없음
@Getter
public class Exception404 extends CustomException {

    public Exception404(ErrorCode errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode.getMessage());
    }

}