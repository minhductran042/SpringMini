package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.UserCreationRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateRequest;
import com.minhductran.tutorial.minhductran.entity.User;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    public User createUser(UserCreationRequest request);
    public List<User> getUsers();
    public User getUser(int userId);
    public User updateUser(int userId, UserUpdateRequest request);
    public void deleteUser(int userId);
}
