package com.minhductran.tutorial.minhductran.dto.response.User;


import com.minhductran.tutorial.minhductran.utils.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserDetailRespone {
    private String username;
    private String phone;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private String email;
    private String logo;
    private Set<String> roles;
}
