package com.example.demo.service;

import java.util.List;

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

    // ✅ CREATE
    @Override
    public UserResponse create(UserRequest request) {

        log.info("Creating user with email: {}", request.getEmail());

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    log.warn("Duplicate email attempt: {}", request.getEmail());
                    throw new RuntimeException("Email already exists");
                });

        User user = UserMapper.toEntity(request);
        user.setId(null);

        return UserMapper.toResponse(userRepository.save(user));
    }

    // ✅ GET ALL
    @Override
    public List<UserResponse> getAll() {

        log.info("Fetching all users");

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    // ✅ GET BY ID
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

    // ✅ UPDATE
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

        return UserMapper.toResponse(userRepository.save(user));
    }

    // ✅ DELETE
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
