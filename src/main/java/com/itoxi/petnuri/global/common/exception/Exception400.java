package com.itoxi.petnuri.global.common.exception;

import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception400 extends CustomException {

    public Exception400(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode.getMessage());
    }

    public Exception400(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}