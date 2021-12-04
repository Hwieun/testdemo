package com.naver.line.demo.common.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
