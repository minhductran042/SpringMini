package com.minhductran.tutorial.minhductran.dto.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailRespone {
    private String username;
    private String phone;
    private String firstName;
    private String lastName;
}
