package com.minhductran.tutorial.minhductran.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserUpdateDTO {
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
}
