package com.example.backend.exception;

import org.springframework.http.HttpStatus;

public class IllegalArgumentException extends BackendException{

    public IllegalArgumentException() {
        super(HttpStatus.BAD_REQUEST,Message.ILLEGAL_ARGUMENT);
    }
}
