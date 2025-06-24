package com.minhductran.tutorial.minhductran.service.impl;


import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.UserDetailRespone;
import com.minhductran.tutorial.minhductran.mappers.UserMapper;
import com.minhductran.tutorial.minhductran.model.User;
import com.minhductran.tutorial.minhductran.exception.ResourceNotFoundException;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import com.minhductran.tutorial.minhductran.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        log.info("Creating user successfully");
        return userRepository.save(user);
    }

    @Override
    public UserDetailRespone getUser(int userId) {
        User user = getUserById(userId);
        return userMapper.toUserDetailResponse(user);
    }

    @Override
    public List<UserDetailRespone> getAllUsers(int pageNo, int pageSize, String sortBy, String sortOrder) {

        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<User> users = userRepository.findAll(pageable);
        return users.stream().map(userMapper::toUserDetailResponse).toList();
    }

    @Override
    public UserDetailRespone updateUser(int userId, UserUpdateDTO request) {
        User user = getUserById(userId);
        userMapper.updateEntity(user, request);
        log.info("Update user successfully");
        userRepository.save(user);
        return userMapper.toUserDetailResponse(user);

    }

    @Override
    public void deleteUser(int userId) {
        getUserById(userId); // Check xem user co ton tai khong
        userRepository.deleteById(userId);
        log.info("Delete user successfully, userId={}", userId);
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
