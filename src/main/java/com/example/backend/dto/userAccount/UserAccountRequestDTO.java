package com.example.backend.dto.userAccount;

import com.example.backend.entity.userAccount.UserAccount;
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
public class UserAccountRequestDTO {
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private int address;
    private UserRole userRole; // UserRole 필드 추가

    public UserAccount toUserAccount(PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .address(address)
                .userRole(userRole != null ? userRole : UserRole.ROLE_USER) // userRole이 null이 아닐 때 설정
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, password);
    }
}
