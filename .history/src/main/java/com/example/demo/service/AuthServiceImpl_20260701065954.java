package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    // ✅ LOGIN
    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String accessToken = jwtService.generateToken(request.getEmail());

        String refreshToken = refreshTokenService
                .createToken(request.getEmail())
                .getToken();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // ✅ REFRESH
    @Override
    public AuthResponse refresh(String refreshToken) {

        var token = refreshTokenService.validate(refreshToken);

        String newAccessToken = jwtService.generateToken(token.getEmail());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // ✅ LOGOUT
    @Override
    public void logout(String refreshToken) {

        log.info("Logging out with refresh token: {}", refreshToken);

        refreshTokenService.delete(refreshToken);
    }
}
