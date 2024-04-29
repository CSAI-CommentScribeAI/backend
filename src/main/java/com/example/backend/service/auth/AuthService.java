package com.example.backend.service.auth;


import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.jwt.TokenStore;
import com.example.backend.jwt.dto.TokenDto;
import com.example.backend.dto.userAccount.UserAccountRequestDto;
import com.example.backend.dto.userAccount.UserAccountResponseDto;
import com.example.backend.jwt.TokenProvider;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;  // 30분


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

    // AuthService의 refresh 메소드를 새로운 generateAccessToken 메소드를 사용하도록 수정
    public TokenDto refresh(String refreshToken) {
        // 리프레시 토큰 유효성 검사
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        String userId = authentication.getName();
        String storedRefreshToken = tokenStore.getRefreshToken(userId);

        if (storedRefreshToken != null && storedRefreshToken.equals(refreshToken)) {
            // 새로운 액세스 토큰만 생성
            String newAccessToken = tokenProvider.generateAccessToken(authentication);

            // 리프레시 토큰의 남은 유효 시간 계산
            long refreshTokenExpiryTime = tokenProvider.getRemainingTime(refreshToken);

            // 새로운 액세스 토큰과 원래의 리프레시 토큰 반환
            return TokenDto.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(newAccessToken)
                    .tokenExpiresIn(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME)
                    .refreshToken(refreshToken) // 원래의 리프레시 토큰 반환
                    .refreshTokenExpiresIn(System.currentTimeMillis() + refreshTokenExpiryTime) // 정확한 만료 시간 설정
                    .build();
        } else {
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
