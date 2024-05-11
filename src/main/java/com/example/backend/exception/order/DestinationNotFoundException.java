package com.example.backend.exception.order;

import com.example.backend.exception.Message;
import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;

public class DestinationNotFoundException extends BackendException {
    public DestinationNotFoundException() {
        super(HttpStatus.BAD_REQUEST, Message.DESTINATION_NOT_FOUND);
    }
}

