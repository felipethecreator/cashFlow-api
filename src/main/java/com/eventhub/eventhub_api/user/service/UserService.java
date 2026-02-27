package com.eventhub.eventhub_api.user.service;

import com.eventhub.eventhub_api.user.dto.input.RegisterRequest;
import com.eventhub.eventhub_api.user.dto.mapper.UserMapper;
import com.eventhub.eventhub_api.user.dto.output.UserResponse;
import com.eventhub.eventhub_api.user.entity.User;
import com.eventhub.eventhub_api.user.entity.UserRole;
import com.eventhub.eventhub_api.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("O email informado já está cadastrado.");
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.PARTICIPANT);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
}
