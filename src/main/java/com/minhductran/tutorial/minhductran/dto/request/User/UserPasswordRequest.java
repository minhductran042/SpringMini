package com.minhductran.tutorial.minhductran.dto.request.User;

import lombok.Getter;

@Getter
public class UserPasswordRequest {
    private String username;
    private String password;
    private String confirmPassword;
}
