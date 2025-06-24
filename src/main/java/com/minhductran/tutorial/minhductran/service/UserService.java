package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.UserDetailRespone;
import com.minhductran.tutorial.minhductran.model.User;

import java.util.List;

public interface UserService {
    public User createUser(UserCreationDTO request);
    public List<UserDetailRespone> getAllUsers(int pageNo, int pageSize);
    public UserDetailRespone getUser(int userId);
    public UserDetailRespone updateUser(int userId, UserUpdateDTO request);
    public void deleteUser(int userId);
}
