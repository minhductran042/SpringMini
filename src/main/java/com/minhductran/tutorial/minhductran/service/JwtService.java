package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.utils.TokenType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {
    String generateAccessToken(int userId, String username, Collection<? extends GrantedAuthority> authorities);
    String generateRefreshToken(int userId, String username, Collection<? extends GrantedAuthority> authorities);
    String extractUsername(String token, TokenType tokenType);
}
