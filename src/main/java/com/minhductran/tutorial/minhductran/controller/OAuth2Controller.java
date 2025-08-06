package com.minhductran.tutorial.minhductran.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {

    @GetMapping("/google")
    public ResponseEntity<Map<String, String>> getGoogleLoginUrl() {
        log.info("Generating Google OAuth2 login URL");
        
        // This endpoint can be used to get the OAuth2 login URL
        // In a real application, you might want to generate this dynamically
        Map<String, String> response = new HashMap<>();
        response.put("message", "Redirect to /oauth2/authorization/google to start OAuth2 login");
        response.put("loginUrl", "/oauth2/authorization/google");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getOAuth2Status() {
        log.info("Checking OAuth2 configuration status");
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "OAuth2 is configured and ready");
        response.put("providers", "Google");
        
        return ResponseEntity.ok(response);
    }
} 