package com.minhductran.tutorial.minhductran.service.impl;


import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.mappers.UserMapper;
import com.minhductran.tutorial.minhductran.model.User;
import com.minhductran.tutorial.minhductran.exception.ResourceNotFoundException;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import com.minhductran.tutorial.minhductran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    // Implement the methods from UserService interface here
    @Override
    public User createUser(UserCreationDTO request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceNotFoundException("USERNAME ALREADY EXISTS");
        }
//        User user = User.builder()
//                .username(request.getUsername())
//                .password(request.getPassword())
//                .phone(request.getPhone())
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .build();
        User user = userMapper.toEntity(request);

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
    public User updateUser(int userId, UserUpdateDTO request) {
        if(userRepository.findById(userId).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        User user = getUser(userId);
        userMapper.updateEntity(user, request);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        if(userRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

}
