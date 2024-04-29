package com.example.backend.exception.store;

import com.example.backend.exception.BackendException;
import com.example.backend.exception.Message;
import org.springframework.http.HttpStatus;

public class StoreNotFoundException extends BackendException {

    public StoreNotFoundException() {
        super(HttpStatus.BAD_REQUEST, Message.STORE_NOT_FOUND);
    }
}