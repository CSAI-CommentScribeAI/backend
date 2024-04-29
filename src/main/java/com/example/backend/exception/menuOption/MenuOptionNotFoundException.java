package com.example.backend.exception.menuOption;

import com.example.backend.exception.BackendException;
import com.example.backend.exception.Message;
import org.springframework.http.HttpStatus;

public class MenuOptionNotFoundException extends BackendException {
    public MenuOptionNotFoundException() {
        super(HttpStatus.NOT_FOUND, Message.MENU_OPTION_NOT_FOUND);
    }
}
