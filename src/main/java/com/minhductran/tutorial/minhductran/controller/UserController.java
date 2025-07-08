package com.minhductran.tutorial.minhductran.controller;
import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserPasswordRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ResponseErrorEntity;
import com.minhductran.tutorial.minhductran.dto.response.ResponseEntity;
import com.minhductran.tutorial.minhductran.dto.response.UserDetailRespone;
import com.minhductran.tutorial.minhductran.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    ResponseEntity<UserDetailRespone> createUser(@RequestBody @Valid UserCreationDTO request) {
        log.info("Request to create user, {} {}", request.getFirstName(), request.getLastName());
        try {
            UserDetailRespone user = userService.createUser(request);
            return new ResponseEntity<>(HttpStatus.CREATED.value(), "User created successfully", user);
        } catch (Exception e) {
            return new ResponseErrorEntity(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("getAll")
    ResponseEntity<List<UserDetailRespone>> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                        @RequestParam (defaultValue = "20", required = false) int pageSize,
                                                        @RequestParam (defaultValue = "id", required = false) String sortBy,
                                                        @RequestParam (defaultValue = "asc", required = false) String sortOrder) {
        try {
            List<UserDetailRespone> users = userService.getAllUsers(pageNo, pageSize, sortBy, sortOrder);
            return new ResponseEntity<List<UserDetailRespone>>(HttpStatus.OK.value(),
                    "Get all users successfully", users);
        } catch (Exception e) {
            log.error("Error getting all users: {}", e.getMessage());
            return new ResponseErrorEntity(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping("get/{userId}")
    ResponseEntity<UserDetailRespone> getUser(@PathVariable int userId) {
        try {
            UserDetailRespone user = userService.getUser(userId);
            return new ResponseEntity<UserDetailRespone>(HttpStatus.OK.value(),
                    "Get user successfully", user);
        } catch (Exception e) {
            log.error("Error getting user with ID {}: {}", userId, e.getMessage());
            return new ResponseErrorEntity(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

    }

    @PutMapping("update/{userId}")
    ResponseEntity<UserDetailRespone> updateUser(@PathVariable int userId,
                                                 @RequestPart MultipartFile multipartFile,
                                                 @RequestBody @Valid UserUpdateDTO request ) {
        UserDetailRespone user = userService.updateUser(userId, request);
        return new ResponseEntity<UserDetailRespone>(HttpStatus.OK.value(), "User updated successfully", user);
    }

    @DeleteMapping("delete/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable int userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
        } catch (Exception e) {
            return new ResponseErrorEntity(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity uploadFile(@PathVariable int userId ,
                                     @RequestParam("image") MultipartFile multipartFile) {
        try {
            userService.uploadImage(userId, multipartFile);
            return new ResponseEntity(HttpStatus.OK.value(), "Image uploaded successfully");
        }
        catch (Exception e) {
            log.error("Error uploading image for user with ID {}: {}", userId, e.getMessage());
            return new ResponseErrorEntity(HttpStatus.BAD_REQUEST.value(), "Failed to upload image: " + e.getMessage());
        }
    }

    @PatchMapping("change-password")
    public ResponseEntity changePassword(@RequestBody @Valid UserPasswordRequest request) {
        try {
            userService.changePassword(request);
            return new ResponseEntity<>(HttpStatus.OK.value(), "Password changed successfully");
        } catch (Exception e) {
            return new ResponseErrorEntity(HttpStatus.BAD_REQUEST.value(), "Failed to change password: " + e.getMessage());
        }
    }
}


