package com.itoxi.petnuri.global.error.exception;

import com.itoxi.petnuri.global.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 인증 안됨
@Getter
public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }

    public ApiResponse.Result<?> body() {
        return ApiResponse.error(getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}