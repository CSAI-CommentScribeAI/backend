package com.example.backend.service.auth;

import com.example.backend.dto.userAccount.CustomOAuth2User;
import com.example.backend.dto.userAccount.GoogleResponse;
import com.example.backend.dto.userAccount.NaverResponse;
import com.example.backend.dto.userAccount.OAuth2Response;
import com.example.backend.dto.userAccount.OAuth2UserDTO;

import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import java.util.Optional;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountRepository userAccountRepository;

    public CustomOAuth2UserService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        }else{
            return null;
        }

        String userId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        Optional<UserAccount> existData = userAccountRepository.findByUserId(userId);
        if (existData.isEmpty()) {
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(userId);
            userAccount.setEmail(oAuth2Response.getEmail());
            userAccount.setNickname(oAuth2Response.getName());
            userAccount.setUserRole(UserRole.valueOf("ROLE_USER"));
            userAccountRepository.save(userAccount);

            OAuth2UserDTO oAuth2UserDTO = new OAuth2UserDTO();
            oAuth2UserDTO.setEmail(oAuth2Response.getEmail());
            oAuth2UserDTO.setName(oAuth2Response.getName());
            oAuth2UserDTO.setUserId(userId);
            oAuth2UserDTO.setRole("ROLE_USER");


            return new CustomOAuth2User(oAuth2UserDTO);
        }else {

            OAuth2UserDTO oAuth2UserDTO = new OAuth2UserDTO();
            oAuth2UserDTO.setEmail(oAuth2Response.getEmail());
            oAuth2UserDTO.setName(oAuth2Response.getName());
            oAuth2UserDTO.setUserId(userId);
            oAuth2UserDTO.setRole("ROLE_USER");


            return new CustomOAuth2User(oAuth2UserDTO);
        }
    }
}
