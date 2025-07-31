package com.minhductran.tutorial.minhductran.config;

import com.minhductran.tutorial.minhductran.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Skip authentication for public endpoints
        if (isPublicEndpoint(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if Authorization header exists and has Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No valid Authorization header found for request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract JWT token
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            
            log.debug("Processing JWT token for user: {}", userEmail);

            // Check if user is already authenticated
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (userEmail != null && authentication == null) {
                // Load user details from database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // Validate JWT token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    log.debug("Token is valid for user: {}", userEmail);
                    
                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication set successfully for user: {}", userEmail);
                } else {
                    log.warn("Invalid token for user: {}", userEmail);
                }
            }

            filterChain.doFilter(request, response);
            
        } catch (Exception exception) {
            log.error("Error processing JWT token: {}", exception.getMessage());
            // Don't set error response here, let the request continue
            // The security configuration will handle unauthorized access
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Check if the request URI is a public endpoint that doesn't require authentication
     */
    private boolean isPublicEndpoint(String requestURI) {
        return requestURI.equals("/auth/login") || 
               requestURI.equals("/auth/register") ||
               requestURI.startsWith("/error") ||
               requestURI.startsWith("/swagger") ||
               requestURI.startsWith("/v3/api-docs");
    }
}