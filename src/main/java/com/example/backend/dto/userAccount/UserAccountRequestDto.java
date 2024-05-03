package com.example.backend.dto.userAccount;

import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.entity.userAccount.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountRequestDto {
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private UserAddress userAddress;
    private UserRole userRole; // UserRole 필드 추가

    public UserAccount toUserAccount(PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .userAddress(userAddress) // Corrected field name
                .userRole(userRole != null ? userRole : UserRole.ROLE_USER)
                .build();
    }


    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, password);
    }
}
