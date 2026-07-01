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

    // ✅ ✅ REFRESH LOGIC
    @Override
    public AuthResponse refresh(String refreshToken) {

        // ✅ validate refresh token
        var token = refreshTokenService.validate(refreshToken);

        // ✅ generate new access token
        String newAccessToken = jwtService.generateToken(token.getEmail());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // reuse same
                .build();
    }
}
