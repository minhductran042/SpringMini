package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.request.UserCreationRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateRequest;
import com.minhductran.tutorial.minhductran.entity.User;
import com.minhductran.tutorial.minhductran.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    User createUser(@RequestBody UserCreationRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable int userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId,request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
