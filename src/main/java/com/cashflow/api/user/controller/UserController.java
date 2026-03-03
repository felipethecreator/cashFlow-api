package com.cashflow.api.user.controller;

import com.cashflow.api.user.dto.input.LoginRequest;
import com.cashflow.api.user.dto.input.RegisterRequest;
import com.cashflow.api.user.dto.output.AuthResponse;
import com.cashflow.api.user.dto.output.UserResponse;
import com.cashflow.api.user.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody RegisterRequest request) throws BadRequestException {
        return userService.createUser(request);
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@Valid @RequestBody LoginRequest request) throws BadRequestException {
        return userService.login(request);
    }

    @GetMapping("/me")
    public String me(Authentication auth) {
        return "Logado como: " + auth.getName();
    }
}
