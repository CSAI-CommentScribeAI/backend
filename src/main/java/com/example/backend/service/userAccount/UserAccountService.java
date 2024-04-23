package com.example.backend.service.userAccount;

import com.example.backend.config.security.SecurityUtil;
import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.dto.UserAccountResponseDto;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountResponseDto getMyInfoBySecurity() {
        return userAccountRepository.findById(SecurityUtil.getCurrentUserId())
                .map(UserAccountResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public UserAccountResponseDto changeUserAccountNickname(String userId, String nickname) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        userAccount.setNickname(nickname);
        return UserAccountResponseDto.of(userAccountRepository.save(userAccount));
    }

    @Transactional
    public UserAccountResponseDto changeUserAccountPassword(Authentication authentication, String password, String newPassword) {
        if (authentication == null) {
            throw new RuntimeException("Authentication information is not available.");
        }

        long userId = Long.parseLong(authentication.getName());
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        authUser.setPassword(passwordEncoder.encode(newPassword));
        return UserAccountResponseDto.of(userAccountRepository.save(authUser));
    }
}
