package com.example.backend.exception.aws;

import com.example.backend.exception.Message;
import com.example.backend.exception.BackendException;
import org.springframework.http.HttpStatus;

public class FileUploadFailedException extends BackendException {
    public FileUploadFailedException() {
        super(HttpStatus.BAD_REQUEST, Message.FILE_UPLOAD_FAILED);
    }
}
