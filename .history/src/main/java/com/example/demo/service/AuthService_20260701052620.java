package com.example.demo.service;

import com.example.demo.dto.*;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}
