package com.example.backend.exception.cart;

import com.example.backend.exception.BackendException;
import com.example.backend.exception.Message;
import org.springframework.http.HttpStatus;

public class CartNotFoundException  extends BackendException{
    public CartNotFoundException() {
        super(HttpStatus.BAD_REQUEST, Message.CART_NOT_FOUND );
    }
}

