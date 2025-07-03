package com.minhductran.tutorial.minhductran.controller;
import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ResponeErrorDTO;
import com.minhductran.tutorial.minhductran.dto.response.ResponseData;
import com.minhductran.tutorial.minhductran.dto.response.UserDetailRespone;
import com.minhductran.tutorial.minhductran.model.User;
import com.minhductran.tutorial.minhductran.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("api/users")
@Validated
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("create")
    ResponseData<UserDetailRespone> createUser(@RequestBody @Valid UserCreationDTO request) {
        log.info("Request to create user, {} {}", request.getFirstName(), request.getLastName());
        try {
            UserDetailRespone user = userService.createUser(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User created successfully", user);
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("getAll")
    ResponseData<List<UserDetailRespone>> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                      @RequestParam (defaultValue = "20", required = false) int pageSize,
                                                      @RequestParam (defaultValue = "id", required = false) String sortBy,
                                                      @RequestParam (defaultValue = "asc", required = false) String sortOrder) {
        try {
            List<UserDetailRespone> users = userService.getAllUsers(pageNo, pageSize, sortBy, sortOrder);
            return new ResponseData<List<UserDetailRespone>>(HttpStatus.OK.value(),
                    "Get all users successfully", users);
        } catch (Exception e) {
            log.error("Error getting all users: {}", e.getMessage());
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping("get/{userId}")
    ResponseData<UserDetailRespone> getUser(@PathVariable int userId) {
        try {
            UserDetailRespone user = userService.getUser(userId);
            return new ResponseData<UserDetailRespone>(HttpStatus.OK.value(),
                    "Get user successfully", user);
        } catch (Exception e) {
            log.error("Error getting user with ID {}: {}", userId, e.getMessage());
            return new ResponeErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

    }

    @PutMapping("update/{userId}")
    ResponseData<UserDetailRespone> updateUser(@PathVariable int userId, @RequestBody @Valid UserUpdateDTO request) {
        UserDetailRespone user = userService.updateUser(userId, request);
        return new ResponseData<UserDetailRespone>(HttpStatus.OK.value(), "User updated successfully", user);
    }

    @DeleteMapping("delete/{userId}")
    ResponseData<?> deleteUser(@PathVariable int userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/upload/{userId}")
    public String uploadFile(@PathVariable int userId ,
                           @RequestParam("image") MultipartFile multipartFile) {
        try {
            userService.uploadImage(userId, multipartFile);
            return "Image uploaded successfully for user with ID: " + userId;
        }
        catch (Exception e) {
            log.error("Error uploading image for user with ID {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

}
