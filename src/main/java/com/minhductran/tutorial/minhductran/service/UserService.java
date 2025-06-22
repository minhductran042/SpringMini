package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.model.User;

import java.util.List;

public interface UserService {
    public User createUser(UserCreationDTO request);
    public List<User> getUsers();
    public User getUser(int userId);
    public User updateUser(int userId, UserUpdateDTO request);
    public void deleteUser(int userId);
}
