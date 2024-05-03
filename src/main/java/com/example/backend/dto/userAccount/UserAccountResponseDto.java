package com.example.backend.dto.userAccount;

import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountResponseDto {
    private String email;
    private String nickname;
    private String userRole;
    private UserAddress userAddress;

    public static UserAccountResponseDto of(UserAccount userAccount){
        return UserAccountResponseDto.builder()
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .userRole(userAccount.getUserRole().toString())
                .userAddress(userAccount.getUserAddress())
                .build();
    }
}
