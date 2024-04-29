package com.example.backend.exception;

import org.springframework.http.HttpStatus;

public class BackendException extends RuntimeException{

    private final HttpStatus httpStatus;

    public BackendException(HttpStatus httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
