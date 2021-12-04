package com.naver.line.demo.common;

import org.springframework.http.HttpStatus;

public class ResponseDetail<T> {

    boolean success = true;
    T response = null;
    Error error = null;

    public ResponseDetail(T response) {
        this.success = true;
        this.response = response;
    }

    public ResponseDetail(HttpStatus status, String message) {
        this.success = false;
        this.error = new Error(status.value(), message);
    }

    public static class Error {
        int status;
        String message;

        public Error (int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

}
