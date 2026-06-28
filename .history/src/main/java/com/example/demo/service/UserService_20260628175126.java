package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;

public interface UserService {

    User create(User user);

    //List<User> getAll();
    //User getById(Long id);
    UserResponse getById(Long id);

    void delete(Long id);

    User update(Long id, User user);

    UserResponse update(Long id, UserRequest request);

    UserResponse create(UserRequest request);

    List<UserResponse> getAll();

}
