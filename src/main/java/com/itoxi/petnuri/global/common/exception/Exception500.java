package com.itoxi.petnuri.global.common.exception;

import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class Exception500 extends CustomException {

    public Exception500(ErrorCode errorCode) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.getMessage());
    }

}
