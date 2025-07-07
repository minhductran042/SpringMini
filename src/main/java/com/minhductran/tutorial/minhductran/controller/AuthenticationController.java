package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.request.SignInRequest;
import com.minhductran.tutorial.minhductran.dto.response.TokenResponse;
import com.minhductran.tutorial.minhductran.service.AuthenticationService;
import com.minhductran.tutorial.minhductran.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j(topic = "AUTHENTICATION_CONTROLLER")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/access-token")
    public TokenResponse getAccessToken(@RequestBody SignInRequest request) {
        log.info("Access Token Request");
        return authenticationService.getAccessToken(request);
    }

    @PostMapping("/refresh-token")
    public TokenResponse getRefreshToken(@RequestBody String refreshToken) {
        log.info("Refresh Token Request");
        return TokenResponse.builder()
                .accessToken("ACCESS_NEW_TOKEN")
                .refreshToken("DUMMY_REFRESH_TOKEN")
                .build();
    }
}
