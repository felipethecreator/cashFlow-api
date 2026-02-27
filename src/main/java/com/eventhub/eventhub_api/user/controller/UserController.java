package com.eventhub.eventhub_api.user.controller;

import com.eventhub.eventhub_api.user.dto.input.RegisterRequest;
import com.eventhub.eventhub_api.user.dto.output.UserResponse;
import com.eventhub.eventhub_api.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public UserResponse create(@Valid @RequestBody RegisterRequest request) {
        return userService.createUser(request);
    }
}
