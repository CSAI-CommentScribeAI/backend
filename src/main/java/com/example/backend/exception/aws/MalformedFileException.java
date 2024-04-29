package com.example.backend.exception.aws;

import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;
public class MalformedFileException extends BackendException {
    public MalformedFileException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
