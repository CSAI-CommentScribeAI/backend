package com.example.backend.service.auth;

import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.jwt.dto.TokenDto;
import com.example.backend.UserAccount.dto.UserAccountRequestDto;
import com.example.backend.UserAccount.dto.UserAccountResponseDto;
import com.example.backend.jwt.TokenProvider;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

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

}
