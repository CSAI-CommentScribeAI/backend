package com.example.backend.service.auth;

import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.jwt.TokenStore;
import com.example.backend.jwt.dto.TokenDto;
import com.example.backend.UserAccount.dto.UserAccountRequestDto;
import com.example.backend.UserAccount.dto.UserAccountResponseDto;
import com.example.backend.jwt.TokenProvider;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TokenStore tokenStore;


    public UserAccountResponseDto signup(UserAccountRequestDto requestDto) {
        if (userAccountRepository.existsByUserId(requestDto.getUserId())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        UserAccount userAccount = requestDto.toUserAccount(passwordEncoder);
        userAccount.setCreatedBy(requestDto.getUserId());
        userAccount.setModifiedBy(requestDto.getUserId());
        return UserAccountResponseDto.of(userAccountRepository.save(userAccount));
    }

    public TokenDto login(UserAccountRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }

    public TokenDto refresh(String refreshToken) {
        // 리프레쉬 토큰의 유효성을 검사합니다.
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        // 리프레쉬 토큰에서 사용자 이름(아이디)을 추출합니다.
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        // Redis에 저장된 리프레쉬 토큰을 확인합니다.
        String userId = authentication.getName();
        String storedRefreshToken = tokenStore.getRefreshToken(userId);
        if (storedRefreshToken != null && storedRefreshToken.equals(refreshToken)) {
            // 새로운 액세스 토큰을 생성합니다.
            TokenDto newAccessToken = tokenProvider.generateTokenDto(authentication);
            // 새로운 리프레쉬 토큰을 발급받을 수도 있습니다. 이 부분은 비즈니스 로직에 따라 결정됩니다.
            // (예: 리프레쉬 토큰을 재사용할 것인지, 아니면 새로 발급할 것인지)
            return newAccessToken;
        } else {
            // 저장된 리프레쉬 토큰이 없거나 요청된 토큰과 다르다면 예외를 발생시킵니다.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token does not exist or does not match");
        }
    }


    public void logout(String refreshToken) {
        // 리프레쉬 토큰의 사용자 정보를 추출
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        // Redis에서 리프레쉬 토큰 삭제
        tokenStore.removeRefreshToken(authentication.getName());
    }
}
