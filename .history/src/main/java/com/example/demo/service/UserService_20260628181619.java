package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;

public interface UserService {

    UserResponse create(UserRequest request);

    List<UserResponse> getAll();

    UserResponse getById(Long id);

    UserResponse update(Long id, UserRequest request);

    void delete(Long id);
}
