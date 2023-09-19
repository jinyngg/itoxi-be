package com.itoxi.petnuri.global.exception;

/**
 * author         : matrix
 * date           : 2023-09-18
 * description    :
 */
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super();
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotFoundException(Throwable cause) {
        super(cause);
    }
}
