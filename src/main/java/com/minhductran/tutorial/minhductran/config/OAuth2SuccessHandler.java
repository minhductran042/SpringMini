package com.minhductran.tutorial.minhductran.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhductran.tutorial.minhductran.dto.response.Auth.LoginResponse;
import com.minhductran.tutorial.minhductran.service.JwtService;
import com.minhductran.tutorial.minhductran.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     Authentication authentication) throws IOException, ServletException {
        
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            
            log.info("OAuth2 authentication successful for user: {}", oauth2User.getName());
            
            // Extract user information from OAuth2
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
//            String picture = oauth2User.getAttribute("picture");
            
            log.info("OAuth2 user details - Email: {}, Name: {}", email, name);
            
            // Process OAuth2 user (create or update user in database)
            var user = userService.processOAuth2User(email);
            
            // Generate JWT tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            
            // Get token expiration times from JwtService
            long accessTokenExpiresIn = System.currentTimeMillis() + jwtService.getExpirationTime();
            long refreshTokenExpiresIn = System.currentTimeMillis() + jwtService.getRefreshTokenExpirationTime();
            
            // Create response using constructor
            LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken, accessTokenExpiresIn, refreshTokenExpiresIn);
            loginResponse.setUser(user);
            
            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            // Write response
            String jsonResponse = objectMapper.writeValueAsString(loginResponse);
            response.getWriter().write(jsonResponse);
            
            log.info("OAuth2 login successful for user: {}", email);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
} 