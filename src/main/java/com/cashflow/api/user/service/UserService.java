package com.cashflow.api.user.service;

import com.cashflow.api.category.service.CategoryService;
import com.cashflow.api.common.exceptions.ConflictException;
import com.cashflow.api.config.jwt.JwtService;
import com.cashflow.api.common.exceptions.UnauthorizedException;
import com.cashflow.api.user.dto.input.LoginRequest;
import com.cashflow.api.user.dto.input.RegisterRequest;
import com.cashflow.api.user.dto.mapper.UserMapper;
import com.cashflow.api.user.dto.output.AuthResponse;
import com.cashflow.api.user.dto.output.UserResponse;
import com.cashflow.api.user.entity.User;
import com.cashflow.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CategoryService categoryService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService, CategoryService categoryService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.categoryService = categoryService;
    }

    @Transactional
    public UserResponse createUser(RegisterRequest request) throws UnauthorizedException {
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("O email informado já está cadastrado.");
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(normalizedEmail);

        User saved = userRepository.saveAndFlush(user);
        log.info("Registrando novo usuário: {}", normalizedEmail);

        categoryService.createDefaultCategories(saved.getId());

        return userMapper.toDto(saved);
    }

    public AuthResponse login(LoginRequest request) throws UnauthorizedException {
        String email = request.getEmail().toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Email ou senha inválidos"));

        boolean ok = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!ok) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toDto(user);
        return new AuthResponse(token, userResponse);
    }
}

