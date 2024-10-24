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
public class UserAccountResponseDTO {
    private String email;
    private String nickname;
    private String userRole;
    private UserAddressDTO address;

    public static UserAccountResponseDTO of(UserAccount userAccount, UserAddress userAddress) {
        return UserAccountResponseDTO.builder()
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .userRole(userAccount.getUserRole().toString())
                .address(UserAddressDTO.toDTO(userAddress))
                .build();
    }
}
