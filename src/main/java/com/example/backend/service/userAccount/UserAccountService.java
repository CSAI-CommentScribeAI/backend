package com.example.backend.service.userAccount;

import com.example.backend.config.security.SecurityUtil;
import com.example.backend.dto.userAccount.UserAccountResponseDTO;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.UserAccount.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAddressRepository userAddressRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountResponseDTO getMyInfoBySecurity() {
        UserAccount userAccount = userAccountRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        UserAddress userAddress = userAddressRepository.findById((long) userAccount.getAddress()).orElse(null);
        return UserAccountResponseDTO.of(userAccount, userAddress);
    }

    @Transactional
    public UserAccountResponseDTO changeUserAccountNickname(String userId, String nickname) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        userAccount.setNickname(nickname);
        UserAddress userAddress = userAddressRepository.findById((long) userAccount.getAddress()).orElse(null);

        return UserAccountResponseDTO.of(userAccountRepository.save(userAccount), userAddress);
    }

    @Transactional
    public UserAccountResponseDTO changeUserAccountPassword(Authentication authentication, String password, String newPassword) {
        if (authentication == null) {
            throw new RuntimeException("Authentication information is not available.");
        }

        long userId = Long.parseLong(authentication.getName());
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        authUser.setPassword(passwordEncoder.encode(newPassword));
        UserAddress userAddress = userAddressRepository.findById((long) authUser.getAddress()).orElse(null);

        return UserAccountResponseDTO.of(userAccountRepository.save(authUser), userAddress);
    }
}
