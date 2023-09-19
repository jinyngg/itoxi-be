package com.itoxi.petnuri.global.exception;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
public class IdNotfoundException extends RuntimeException {

    public IdNotfoundException() {
        super();
    }

    public IdNotfoundException(String message) {
        super(message);
    }

    public IdNotfoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotfoundException(Throwable cause) {
        super(cause);
    }
}
