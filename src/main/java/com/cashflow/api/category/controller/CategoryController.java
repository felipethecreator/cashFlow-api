package com.cashflow.api.category.controller;

import com.cashflow.api.category.dto.input.CreateCategory;
import com.cashflow.api.category.dto.output.CategoryResponse;
import com.cashflow.api.category.service.CategoryService;
import com.cashflow.api.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateCategory request
    ) {
        return categoryService.createCategory(user.getId(), request);
    }
}
