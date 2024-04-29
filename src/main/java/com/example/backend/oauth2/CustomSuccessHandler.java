package com.example.backend.oauth2;

import com.example.backend.UserAccount.dto.CustomOAuth2User;
import com.example.backend.jwt.TokenProvider;
import com.example.backend.jwt.dto.TokenDto;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    public CustomSuccessHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        TokenDto token = tokenProvider.generateTokenDto(authentication);

        response.addCookie(createCookie("accessToken", token.getAccessToken()));
        response.addCookie(createCookie("refreshToken", token.getRefreshToken()));
        response.sendRedirect("http://localhost:3000");
    }

    private Cookie createCookie(String Key, String value) {
        Cookie cookie = new Cookie(Key, value);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        return cookie;
    }
}
