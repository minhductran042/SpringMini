package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.request.User.LoginUserDto;
import com.minhductran.tutorial.minhductran.dto.response.LoginResponse;
import com.minhductran.tutorial.minhductran.model.User;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import com.minhductran.tutorial.minhductran.service.AuthenticationService;
import com.minhductran.tutorial.minhductran.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Data
    public static class UserResponse {
        private int id;
        private String username;
        private String email;
        private String phone;
        private String firstName;
        private String lastName;
        private String status;
        private String logo;
        private java.util.List<String> authorities;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    // Thêm endpoint để lấy thông tin user hiện tại
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();
            UserResponse response = new UserResponse();
            response.setId(currentUser.getId());
            response.setUsername(currentUser.getUsername());
            response.setEmail(currentUser.getEmail());
            response.setPhone(currentUser.getPhone());
            response.setFirstName(currentUser.getFirstName());
            response.setLastName(currentUser.getLastName());
            response.setStatus(currentUser.getStatus() != null ? currentUser.getStatus().name() : null);
            response.setLogo(currentUser.getLogo());
            response.setAuthorities(currentUser.getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(response);
        } else if (authentication != null && authentication.getPrincipal() instanceof String) {
            // If principal is a string (username), we need to load the user from database
            String username = (String) authentication.getPrincipal();
            User user = userRepository.findByEmail(username).orElse(null);
            if (user != null) {
                UserResponse response = new UserResponse();
                response.setId(user.getId());
                response.setUsername(user.getUsername());
                response.setEmail(user.getEmail());
                response.setPhone(user.getPhone());
                response.setFirstName(user.getFirstName());
                response.setLastName(user.getLastName());
                response.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
                response.setLogo(user.getLogo());
                response.setAuthorities(user.getAuthorities().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList()));
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).build();
    }
}
