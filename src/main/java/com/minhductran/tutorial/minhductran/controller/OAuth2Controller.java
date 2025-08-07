package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.response.Auth.LoginResponse;
import com.minhductran.tutorial.minhductran.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {

    @Value("${GOOGLE_CLIENT_ID:}")
    private String googleClientId;

    @Value("${GITHUB_CLIENT_ID:}")
    private String githubClientId;

    @GetMapping("/google")
    public ResponseEntity<Map<String, String>> getGoogleLoginUrl() {
        log.info("Generating Google OAuth2 login URL");

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
        response.put("providers", "Google, GitHub");

        response.put("google_client_id_configured", !googleClientId.isEmpty() ? "true" : "false");
        response.put("github_client_id_configured", !githubClientId.isEmpty() ? "true" : "false");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/github")
    public ResponseEntity<Map<String, String>> getGithubLoginUrl() {
        log.info("Generating GitHub OAuth2 login URL");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Redirect to /oauth2/authorization/github to start OAuth2 login");
        response.put("loginUrl", "/oauth2/authorization/github");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<?> oauth2Success(HttpServletRequest request) {
        log.info("OAuth2 success endpoint called");
        log.info("Session ID: {}", request.getSession().getId());
        log.info("Session is new: {}", request.getSession().isNew());

        var session = request.getSession(false);
        if (session != null) {
            log.info("Session attributes: {}", session.getAttributeNames());
            for (String attributeName : Collections.list(session.getAttributeNames())) {
                log.info("Session attribute {}: {}", attributeName, session.getAttribute(attributeName));
            }
        } else {
            log.warn("No session found");
        }

        LoginResponse loginResponse = null;
        User user = null;

        if (session != null) {
            loginResponse = (LoginResponse) session.getAttribute("oauth2_tokens");
            user = (User) session.getAttribute("oauth2_user");
        }

        if (loginResponse != null && user != null) {
            session.removeAttribute("oauth2_tokens");
            session.removeAttribute("oauth2_user");

            log.info("Returning OAuth2 login response for user: {}", user.getEmail());
            return ResponseEntity.ok(loginResponse);
        } else {
            log.warn("No tokens or user found in session");
            log.warn("loginResponse is null: {}", loginResponse == null);
            log.warn("user is null: {}", user == null);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "OAuth2 authentication successful but no tokens found");
            response.put("session_id", session != null ? session.getId() : "no_session");
            response.put("has_tokens", loginResponse != null ? "true" : "false");
            response.put("has_user", user != null ? "true" : "false");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/failure")
    public ResponseEntity<Map<String, String>> oauth2Failure() {
        log.info("OAuth2 authentication failed");

        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "OAuth2 authentication failed");

        return ResponseEntity.badRequest().body(response);
    }
} 