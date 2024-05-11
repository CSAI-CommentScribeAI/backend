package com.example.backend.exception.order;

import com.example.backend.exception.Message;
import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;

public class OrderTimeIndexIllegalArgumentException extends BackendException {
    public OrderTimeIndexIllegalArgumentException() {
        super(HttpStatus.BAD_REQUEST, Message.ORDER_TIMEINDEX_ILLEGAL);
    }
}
