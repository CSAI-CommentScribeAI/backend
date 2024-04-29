package com.example.backend.controller.auth;

import com.example.backend.dto.userAccount.UserAccountRequestDto;
import com.example.backend.dto.userAccount.UserAccountResponseDto;
import com.example.backend.jwt.dto.TokenDto;
import com.example.backend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserAccountResponseDto> signup(@RequestBody UserAccountRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserAccountRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto tokenDto) {
        TokenDto newTokenDto = authService.refresh(tokenDto.getRefreshToken());
        if (newTokenDto != null) {
            return ResponseEntity.ok(newTokenDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto) {
        authService.logout(tokenDto.getRefreshToken());
        return ResponseEntity.ok().build();  // 성공적인 로그아웃 처리 응답
    }
}
