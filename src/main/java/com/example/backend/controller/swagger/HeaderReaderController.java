package com.example.backend.controller.swagger;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HeaderReaderController {

    @GetMapping
    public ResponseEntity<Object> authHeaderChecker(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>(){{
            put("Authorization", request.getHeader("Authorization"));
        }};
        return ResponseEntity.ok(response);
    }
}
