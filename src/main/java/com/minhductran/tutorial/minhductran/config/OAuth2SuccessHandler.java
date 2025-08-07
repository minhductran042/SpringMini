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
        
        log.info("OAuth2SuccessHandler called with authentication type: {}", authentication.getClass().getSimpleName());
        
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            
            log.info("OAuth2 authentication successful for user: {}", oauth2User.getName());
            
            // Extract user information from OAuth2
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String picture = oauth2User.getAttribute("picture");
            
            // Handle different OAuth2 providers
            String provider = oauthToken.getAuthorizedClientRegistrationId();
            log.info("OAuth2 user details - Provider: {}, Email: {}, Name: {}", provider, email, name);
            
            // Log all available attributes for debugging
            log.info("All OAuth2 attributes: {}", oauth2User.getAttributes());
            
            // Handle GitHub users who don't provide email
            if (email == null || email.isEmpty()) {
                if ("github".equals(provider)) {
                    String login = oauth2User.getAttribute("login");
                    Object githubIdObj = oauth2User.getAttribute("id");
                    String githubId = null;
                    if (githubIdObj != null) {
                        githubId = githubIdObj.toString();
                    }
                    if (login != null && !login.isEmpty()) {
                        // Create unique email from GitHub login and ID
                        email = login + "_" + githubId + "@github.com";
                        log.info("GitHub user without email, using generated email: {}", email);
                    } else {
                        log.error("GitHub user has no login attribute");
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"GitHub user information is incomplete\"}");
                        return;
                    }
                } else {
                    log.error("Email is null or empty for OAuth2 user. Provider: {}", provider);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Email is required but not provided by OAuth2 provider\"}");
                    return;
                }
            }
            
            // Process OAuth2 user (create or update user in database)
            String githubId = null;
            if ("github".equals(provider)) {
                Object githubIdObj = oauth2User.getAttribute("id");
                if (githubIdObj != null) {
                    githubId = githubIdObj.toString();
                }
            }
            var user = userService.processOAuth2User(email, githubId);
            log.info("Processed OAuth2 user: {}", user.getEmail());
            
            // Generate JWT tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            log.info("Generated JWT tokens for user: {}", user.getEmail());
            
            // Get token expiration times from JwtService
            long accessTokenExpiresIn = System.currentTimeMillis() + jwtService.getExpirationTime();
            long refreshTokenExpiresIn = System.currentTimeMillis() + jwtService.getRefreshTokenExpirationTime();
            
            // Create response using constructor
            LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken, accessTokenExpiresIn, refreshTokenExpiresIn);
            loginResponse.setUser(user);
            
            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            // Write response directly
            String jsonResponse = objectMapper.writeValueAsString(loginResponse);
            response.getWriter().write(jsonResponse);
            
            log.info("OAuth2 login successful for user: {}", email);
        } else {
            log.warn("Authentication is not OAuth2AuthenticationToken: {}", authentication.getClass().getSimpleName());
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
} 