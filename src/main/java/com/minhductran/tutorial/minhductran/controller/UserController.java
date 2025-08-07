package com.minhductran.tutorial.minhductran.controller;
import com.minhductran.tutorial.minhductran.dto.request.User.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.User.UserPasswordRequest;
import com.minhductran.tutorial.minhductran.dto.request.User.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import com.minhductran.tutorial.minhductran.dto.response.User.UserDetailRespone;
import com.minhductran.tutorial.minhductran.model.User;
import com.minhductran.tutorial.minhductran.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("create")
    ApiResponse<UserDetailRespone> createUser(@RequestBody @Valid UserCreationDTO request) {

        try {
            UserDetailRespone user = userService.createUser(request);
            return new ApiResponse<>(HttpStatus.CREATED.value(), "User created successfully", user);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("me")
    public org.springframework.http.ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return org.springframework.http.ResponseEntity.ok(currentUser);
    }

    @GetMapping("list")
    ApiResponse<List<UserDetailRespone>> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                     @RequestParam (defaultValue = "20", required = false) int pageSize,
                                                     @RequestParam (defaultValue = "id", required = false) String sortBy,
                                                     @RequestParam (defaultValue = "asc", required = false) String sortOrder) {
        try {
            List<UserDetailRespone> users = userService.getAllUsers(pageNo, pageSize, sortBy, sortOrder);
            return new ApiResponse<List<UserDetailRespone>>(HttpStatus.OK.value(),
                    "Get all users successfully", users);
        } catch (Exception e) {
            log.error("Error getting all users: {}", e.getMessage());
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping("get/{userId}")
    ApiResponse<UserDetailRespone> getUser(@PathVariable int userId) {
        try {
            UserDetailRespone user = userService.getUser(userId);
            return new ApiResponse<UserDetailRespone>(HttpStatus.OK.value(),
                    "Get user successfully", user);
        } catch (Exception e) {
            log.error("Error getting user with ID {}: {}", userId, e.getMessage());
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

    }

    @PutMapping("update/{userId}")
    ApiResponse<UserDetailRespone> updateUser(@PathVariable int userId,
                                              @RequestPart MultipartFile multipartFile,
                                              @RequestBody @Valid UserUpdateDTO request ) {
        UserDetailRespone user = userService.updateUser(userId, request);
        return new ApiResponse<UserDetailRespone>(HttpStatus.OK.value(), "User updated successfully", user);
    }

    @DeleteMapping("delete/{userId}")
    ApiResponse<?> deleteUser(@PathVariable int userId) {
        try {
            userService.deleteUser(userId);
            return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/upload/{userId}")
    public ApiResponse<?> uploadFile(@PathVariable int userId ,
                                  @RequestParam("image") MultipartFile multipartFile) {
        try {
            userService.uploadImage(userId, multipartFile);
            return new ApiResponse<>(HttpStatus.OK.value(), "Image uploaded successfully");
        }
        catch (Exception e) {
            log.error("Error uploading image for user with ID {}: {}", userId, e.getMessage());
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to upload image: " + e.getMessage());
        }
    }

    @PutMapping("change-password")
    public ApiResponse<?> changePassword(@RequestBody @Valid UserPasswordRequest request) {
        try {
            userService.changePassword(request);
            return new ApiResponse<>(HttpStatus.OK.value(), "Password changed successfully");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to change password: " + e.getMessage());
        }
    }

}


