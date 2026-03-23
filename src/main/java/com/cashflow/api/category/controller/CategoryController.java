package com.cashflow.api.category.controller;

import com.cashflow.api.category.dto.input.CreateCategory;
import com.cashflow.api.category.dto.input.UpdateCategory;
import com.cashflow.api.category.dto.output.CategoryResponse;
import com.cashflow.api.common.security.AuthenticatedUser;
import com.cashflow.api.category.service.CategoryService;
import com.cashflow.api.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/categories", "/categories"})
@Tag(name = "Categories", description = "Gerenciamento de categorias de despesas")
@SecurityRequirement(name = "bearer-jwt")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria personalizada")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoria criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Categoria já existe"
            )})
    public ResponseEntity<CategoryResponse> create(
            Authentication authentication,
            @Valid @RequestBody CreateCategory request
    ) {
        User user = AuthenticatedUser.require(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "Listar minhas categorias", description = "Retorna todas as categorias do usuário autenticado")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categorias listadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado"
            )})
    public ResponseEntity<List<CategoryResponse>> getMyCategories(
            Authentication authentication
    ) {
        User user = AuthenticatedUser.require(authentication);
        return ResponseEntity.ok(categoryService.getUserCategories(user.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Nome já existe")
    })
    public ResponseEntity<CategoryResponse> updateCategory(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategory request
    ) {
        User user = AuthenticatedUser.require(authentication);
        return ResponseEntity.ok(categoryService.updateCategory(user.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria deletada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<Void> deleteCategory(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        User user = AuthenticatedUser.require(authentication);
        categoryService.deleteCategory(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

}
