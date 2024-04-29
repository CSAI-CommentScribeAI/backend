package com.example.backend.exception.menu;

import com.example.backend.exception.BackendException;
import com.example.backend.exception.Message;
import org.springframework.http.HttpStatus;

public class MenuNotFoundException extends BackendException {

    public MenuNotFoundException() {
        super(HttpStatus.BAD_REQUEST, Message.MENU_NOT_FOUND);
    }
}

