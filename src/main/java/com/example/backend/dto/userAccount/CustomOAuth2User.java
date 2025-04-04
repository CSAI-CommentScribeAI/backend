package com.example.backend.dto.userAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserDTO oAuth2UserDTO;

    public CustomOAuth2User(OAuth2UserDTO oAuth2UserDTO) {
        this.oAuth2UserDTO = oAuth2UserDTO;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return oAuth2UserDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return oAuth2UserDTO.getName();
    }

    public String getUserId() {

        return oAuth2UserDTO.getUserId();
    }
}
