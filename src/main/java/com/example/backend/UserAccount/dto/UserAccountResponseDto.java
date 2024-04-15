package com.example.backend.UserAccount.dto;

import com.example.backend.UserAccount.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountResponseDto {
    private String nickname;
    private String phone;
    private String userRole;
    private int address;

    public static UserAccountResponseDto of(UserAccount userAccount){
        return UserAccountResponseDto.builder()
                .nickname(userAccount.getNickname())
                .phone(userAccount.getPhone())
                .userRole(userAccount.getUserRole().toString())
                .address(userAccount.getAddress())
                .build();
    }
}
