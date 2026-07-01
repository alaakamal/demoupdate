package com.example.demo.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ CREATE (REGISTER USER)
    @Override
    public UserResponse create(UserRequest request) {

        log.info("Creating user with email: {}", request.getEmail());

        // ✅ check duplicate email
        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    log.warn("Duplicate email attempt: {}", request.getEmail());
                    throw new RuntimeException("Email already exists");
                });

        // ✅ map DTO → Entity
        User user = UserMapper.toEntity(request);
        user.setId(null);

        // ✅ ✅ VERY IMPORTANT: encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return UserMapper.toResponse(userRepository.save(user));
    }

    // ✅ GET ALL USERS
    @Override
    public List<UserResponse> getAll() {

        log.info("Fetching all users");

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    // ✅ GET USER BY ID
    @Override
    public UserResponse getById(Long id) {

        log.info("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new RuntimeException("User not found");
                });

        return UserMapper.toResponse(user);
    }

    // ✅ UPDATE USER
    @Override
    public UserResponse update(Long id, UserRequest request) {

        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found for update: {}", id);
                    return new RuntimeException("User not found");
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // ✅ optional password update
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    // ✅ DELETE USER
    @Override
    public void delete(Long id) {

        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("User not found for delete: {}", id);
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}
