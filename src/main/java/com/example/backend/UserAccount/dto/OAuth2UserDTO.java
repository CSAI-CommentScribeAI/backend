package com.example.backend.UserAccount.dto;

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
