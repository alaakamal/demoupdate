package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse login(LoginRequest request) {

        // ✅ Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // ✅ Generate tokens
        String accessToken = jwtService.generateToken(request.getEmail());
        String refreshToken = refreshTokenService
                .createToken(request.getEmail())
                .getToken();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
