package com.example.backend.dto.userAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserDTO {
    private String role;
    private String email;
    private String name;
    private String userId;
}
