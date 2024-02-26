package com.example.backend.UserAccount.dto;

import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.entity.UserRole;
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
    private String email;
    private String password;
    private String nickname;
    private String userName;
    private String phone;
    private String memo;
    private UserRole userRole; // UserRole 필드 추가

    public UserAccount toUserAccount(PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .userId(userId)
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .userName(userName)
                .phone(phone)
                .memo(memo)
                .userRole(userRole != null ? userRole : UserRole.ROLE_USER) // userRole이 null이 아닐 때 설정
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, password);
    }
}
