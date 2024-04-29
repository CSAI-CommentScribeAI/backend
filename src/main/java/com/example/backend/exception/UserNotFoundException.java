package com.example.backend.exception;


import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BackendException {
    public UserNotFoundException() {
        super(HttpStatus.BAD_REQUEST, Message.USER_NOT_FOUND);
    }
}
