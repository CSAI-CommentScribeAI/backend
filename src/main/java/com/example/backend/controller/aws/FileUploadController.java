package com.example.backend.controller.aws;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            // 파일 업로드 처리 코드
            String filename = file.getOriginalFilename();
        } else {
            // 파일이 비어 있거나 null인 경우에 대한 예외 처리
            return ResponseEntity.badRequest().body("File is empty or null");
        }
        return null;
    }
}
