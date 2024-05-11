package com.example.backend.exception.order;


import com.example.backend.exception.Message;
import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BackendException {
    public OrderNotFoundException() {
        super(HttpStatus.BAD_REQUEST, Message.ORDER_NOT_FOUND);
    }
}
