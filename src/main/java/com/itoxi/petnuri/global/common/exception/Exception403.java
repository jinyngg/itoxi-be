package com.itoxi.petnuri.global.common.exception;

import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 권한 없음
@Getter
public class Exception403 extends CustomException {

    public Exception403(ErrorCode errorCode) {
        super(HttpStatus.FORBIDDEN, errorCode.getMessage());
    }

}