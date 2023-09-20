package com.itoxi.petnuri.global.common.exception;

import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 인증 안됨
@Getter
public class Exception401 extends CustomException {

    public Exception401(ErrorCode errorCode) {
        super(HttpStatus.UNAUTHORIZED, errorCode.getMessage());
    }

}