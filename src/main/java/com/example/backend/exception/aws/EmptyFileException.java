package com.example.backend.exception.aws;

import com.example.backend.exception.Message;
import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;

public class EmptyFileException extends BackendException {
    public EmptyFileException() {
        super(HttpStatus.BAD_REQUEST, Message.EMPTY_FILE);
    }
}
