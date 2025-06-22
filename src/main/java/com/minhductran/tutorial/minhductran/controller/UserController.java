package com.minhductran.tutorial.minhductran.controller;
import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ResponeErrorDTO;
import com.minhductran.tutorial.minhductran.dto.response.ResponseData;
import com.minhductran.tutorial.minhductran.model.User;
import com.minhductran.tutorial.minhductran.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    ResponseData<User> createUser(@RequestBody @Valid UserCreationDTO request) {

        try {
            User user = userService.createUser(request);
            return new ResponseData<User>(HttpStatus.CREATED.value(), "User created successfully", user);
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping
    ResponseData<List<User>> getUsers() {
         List<User> users = userService.getUsers();
         return new ResponseData<List<User>>(HttpStatus.OK.value(),
                "User created successfully", users);
    }

    @GetMapping("/{userId}")
    ResponseData<User> getUser(@PathVariable int userId) {
        User user = userService.getUser(userId);
        return new ResponseData<User>(HttpStatus.OK.value(),
                "Get user successfully", user);
    }

    @PutMapping("/{userId}")
    ResponseData<User> updateUser(@PathVariable int userId, @RequestBody UserUpdateDTO request) {
        User user = userService.updateUser(userId, request);
        return new ResponseData<User>(HttpStatus.OK.value(), "User updated successfully", user);
    }

    @DeleteMapping("/{userId}")
    ResponseData<?> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
    }
}
