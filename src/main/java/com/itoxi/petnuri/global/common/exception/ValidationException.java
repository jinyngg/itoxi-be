package com.itoxi.petnuri.global.common.exception;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.VALIDATION_ERROR;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationException extends CustomException {

    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }

    private final List<ValidationError> validationErrors;

    public ValidationException(List<ValidationError> validationErrors) {
        super(HttpStatus.BAD_REQUEST, VALIDATION_ERROR.getMessage());
        this.validationErrors = validationErrors;
    }

}
