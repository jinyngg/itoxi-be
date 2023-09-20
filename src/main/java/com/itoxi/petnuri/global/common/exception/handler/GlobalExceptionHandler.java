package com.itoxi.petnuri.global.common.exception.handler;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.INTERNAL_SERVER_ERROR;

import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.Exception401;
import com.itoxi.petnuri.global.common.exception.Exception403;
import com.itoxi.petnuri.global.common.exception.Exception404;
import com.itoxi.petnuri.global.common.exception.Exception500;
import com.itoxi.petnuri.global.common.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<Object> badRequest(Exception400 e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<Object> unAuthorized(Exception401 e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<Object> forbidden(Exception403 e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<Object> notFound(Exception404 e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<Object> serverError(Exception500 e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(INTERNAL_SERVER_ERROR.getMessage()));
    }

}
