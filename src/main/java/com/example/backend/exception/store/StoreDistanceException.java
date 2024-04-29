package com.example.backend.exception.store;


import com.example.backend.exception.BackendException;
import com.example.backend.exception.Message;
import org.springframework.http.HttpStatus;

public class StoreDistanceException extends BackendException {
    public StoreDistanceException() {
        super(HttpStatus.BAD_REQUEST, Message.STORE_DISTANCE);
    }
}
