package com.itoxi.petnuri.global.common.exception.handler;

import com.itoxi.petnuri.global.common.exception.ValidationException;
import com.itoxi.petnuri.global.common.exception.ValidationException.ValidationError;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Aspect
@Component
public class ValidationHandler {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {
    }

    @Before("postMapping() || putMapping()")
    public void validationAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        List<ValidationError> validationErrors = new ArrayList<>();

        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;

                if (errors.hasErrors()) {
                    for (FieldError fieldError : errors.getFieldErrors()) {
                        validationErrors.add(new ValidationError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        ));
                    }

                    throw new ValidationException(validationErrors);
                }
            }
        }
    }
}