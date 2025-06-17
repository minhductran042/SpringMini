package com.minhductran.tutorial.minhductran.service.impl;


import com.minhductran.tutorial.minhductran.dto.request.UserCreationRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateRequest;
import com.minhductran.tutorial.minhductran.entity.User;
import com.minhductran.tutorial.minhductran.exception.ResourceNotFoundException;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import com.minhductran.tutorial.minhductran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository userRepository;

    // Implement the methods from UserService interface here
    @Override
    public User createUser(UserCreationRequest request) {
        User user = new User();

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceNotFoundException("USERNAME ALREADY EXISTS");
        }

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("USER NOT FOUND"));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(int userId, UserUpdateRequest request) {
        if(!userRepository.findById(userId).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        User user = getUser(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        if(!userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

}
