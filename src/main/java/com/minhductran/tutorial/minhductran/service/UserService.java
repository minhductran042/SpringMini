package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.UserCreationRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateRequest;
import com.minhductran.tutorial.minhductran.entity.User;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(int id, UserUpdateRequest request) {
        User user = getUser(id);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());

        return userRepository.save(user);

    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
