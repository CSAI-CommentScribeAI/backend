package com.example.backend.exception;

import com.example.backend.exception.Message;
import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;

public class UserOrderDetailNotFoundException extends BackendException {
    public UserOrderDetailNotFoundException() {
        super(HttpStatus.BAD_REQUEST, Message.USER_ORDER_DETAIL_NOT_FOUND);
    }
}