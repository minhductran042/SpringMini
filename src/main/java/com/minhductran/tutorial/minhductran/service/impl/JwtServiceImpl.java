package com.minhductran.tutorial.minhductran.service.impl;

import com.minhductran.tutorial.minhductran.service.JwtService;
import com.minhductran.tutorial.minhductran.utils.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "JwtService")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Override
    public String generateAccessToken(int userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generating JWT token for user: {} with authorities {}", username, authorities);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authoritiesToList(authorities));
        return generateToken(claims, username);
    }

    @Override
    public String generateRefreshToken(int userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generating refresh token for user: {} with authorities {}", username, authorities);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authoritiesToList(authorities));
        return generateRefreshToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        log.info("Extracting username from token: {} of type: {}", token, tokenType);
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    private <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimsResolver) {
        log.info("Extracting claim from token: {} of type: {}", token, tokenType);
        Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType tokenType) {
        log.info("Extracting all claims from token: {} of type: {}", token, tokenType);
        try {
            return Jwts.parser().setSigningKey(getKey(tokenType)).build().parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("AccessDenied " + e.getMessage());
        }
    }

    private String generateToken(Map<String, Object> claims, String username) {
        log.info("Generating JWT token with claims: {} for user: {}", claims, username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )) // 1 hour expiration
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        log.info("Generating Refresh token with claims: {} for user: {}", claims, username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14 )) // 14 days expiration
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> {
                throw new IllegalArgumentException("Invalid token type");
            }
        }
    }

    private List<String> authoritiesToList(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) return Collections.emptyList();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}