package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.SignInRequest;
import com.minhductran.tutorial.minhductran.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);
    TokenResponse getRefreshToken(String request);
}
