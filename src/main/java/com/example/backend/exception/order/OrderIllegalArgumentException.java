package com.example.backend.exception.order;

import com.example.backend.exception.Message;
import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;

public class OrderIllegalArgumentException extends BackendException {
    public OrderIllegalArgumentException() {
        super(HttpStatus.BAD_REQUEST, Message.ORDER_ILLEGAL);
    }
}
