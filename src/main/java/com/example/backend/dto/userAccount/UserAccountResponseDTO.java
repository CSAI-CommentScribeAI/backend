package com.example.backend.dto.userAccount;

import com.example.backend.entity.userAccount.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountResponseDTO {
    private String email;
    private String nickname;
    private String userRole;
    private int address;

    public static UserAccountResponseDTO of(UserAccount userAccount){
        return UserAccountResponseDTO.builder()
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .userRole(userAccount.getUserRole().toString())
                .address(userAccount.getAddress())
                .build();
    }
}
