package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.ToDoDTO;
import com.minhductran.tutorial.minhductran.dto.request.User.UserCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.User.UserPasswordRequest;
import com.minhductran.tutorial.minhductran.dto.request.User.UserUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.User.UserDetailRespone;
import com.minhductran.tutorial.minhductran.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public UserDetailRespone createUser(UserCreationDTO request);
    public List<UserDetailRespone> getAllUsers(int pageNo, int pageSize, String sortBy, String sortOrder);
    public UserDetailRespone getUser(int userId);
    public UserDetailRespone updateUser(int userId,UserUpdateDTO request);
    public void deleteUser(int userId);
    public User createUserWithTodo(UserCreationDTO request, ToDoDTO rquest);
    public void uploadImage(int userId, MultipartFile multipartFile);
    public void changePassword(UserPasswordRequest request);
}
