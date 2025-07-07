package com.minhductran.tutorial.minhductran.service.impl;

import com.minhductran.tutorial.minhductran.dto.request.SignInRequest;
import com.minhductran.tutorial.minhductran.dto.response.TokenResponse;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import com.minhductran.tutorial.minhductran.service.AuthenticationService;
import com.minhductran.tutorial.minhductran.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        log.info("get access token");
        String accessToken =  jwtService.generateAccessToken( 1, request.getUsername(), null);
        String refreshToken = jwtService.generateRefreshToken(1, request.getUsername(), null);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String request) {
        return null;
    }
}
