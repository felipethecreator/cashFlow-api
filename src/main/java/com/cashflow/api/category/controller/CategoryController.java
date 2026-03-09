package com.cashflow.api.category.controller;

import com.cashflow.api.category.dto.input.CreateCategory;
import com.cashflow.api.category.dto.output.CategoryResponse;
import com.cashflow.api.category.entity.Category;
import com.cashflow.api.category.service.CategoryService;
import com.cashflow.api.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Gerenciamento de categorias de despesas")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria personalizada")
    @ApiResponse(responseCode = "409", description = "Categoria já existe")
    public ResponseEntity<CategoryResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateCategory request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "Listar minhas categorias", description = "Retorna todas as categorias do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso")
    public ResponseEntity<List<CategoryResponse>> getMyCategories(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.getUserCategories(user.getId()));
    }
}
