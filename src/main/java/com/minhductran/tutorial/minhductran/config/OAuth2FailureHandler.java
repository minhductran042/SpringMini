package com.minhductran.tutorial.minhductran.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     AuthenticationException exception) throws IOException, ServletException {
        
        log.error("OAuth2 authentication failed: {}", exception.getMessage());
        
        // Create error response
        ApiResponse<String> errorResponse = new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "OAuth2 authentication failed",
                exception.getMessage()
        );
        
        // Set response headers
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Write response
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
} 