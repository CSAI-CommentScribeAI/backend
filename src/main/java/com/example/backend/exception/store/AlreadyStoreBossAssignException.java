package com.example.backend.exception.store;

import com.example.backend.exception.BackendException;
import com.example.backend.exception.Message;

import org.springframework.http.HttpStatus;

public class AlreadyStoreBossAssignException extends BackendException {
    public AlreadyStoreBossAssignException() {
        super(HttpStatus.BAD_REQUEST, Message.ALREADY_STORE_BOSS_ASSIGN);
    }
}
