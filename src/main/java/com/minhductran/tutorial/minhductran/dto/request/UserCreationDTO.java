package com.minhductran.tutorial.minhductran.dto.request;

import com.minhductran.tutorial.minhductran.utils.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserCreationDTO {

    @Size(min = 3, message = "Username must be at least 6 characters")
    private String username;
    @Size(min = 6, message = "Password must be at least 8 characters")
    private String password;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;
    private UserStatus status;
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @NotBlank(message = "Email cannot be blank")
    private String email;

}
