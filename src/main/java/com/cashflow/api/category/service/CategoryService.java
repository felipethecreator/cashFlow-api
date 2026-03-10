package com.cashflow.api.category.service;

import com.cashflow.api.category.dto.input.CreateCategory;
import com.cashflow.api.category.dto.input.UpdateCategory;
import com.cashflow.api.category.dto.mapper.CategoryMapper;
import com.cashflow.api.category.dto.output.CategoryResponse;
import com.cashflow.api.category.entity.Category;
import com.cashflow.api.category.repository.CategoryRepository;
import com.cashflow.api.common.exceptions.ConflictException;
import com.cashflow.api.common.exceptions.NotFoundException;
import com.cashflow.api.common.exceptions.UnauthorizedException;
import com.cashflow.api.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryResponse createCategory(UUID userId, CreateCategory data) {
        if (categoryRepository.existsByUserIdAndName(userId, data.getName())) {
            throw new UnauthorizedException("A categoria informada já existe.");
        }

        Category category = categoryMapper.toEntity(data);
        category.setName(data.getName());
        category.setColor(data.getColor());
        category.setIcon(data.getIcon());
        category.setUser(new User());
        category.getUser().setId(userId);
        Category saved = categoryRepository.save(category);
        log.info("Registrando nova categoria: {}", data.getName());
        return categoryMapper.toDto(saved);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CategoryResponse> getUserCategories(UUID userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Transactional
    public void createDefaultCategories(UUID userId) {
        List<Category> defaultCategories = Arrays.asList(
                buildCategory(userId, "Moradia", "home", "#FF5733"),
                buildCategory(userId, "Alimentação", "utensils", "#33FF57"),
                buildCategory(userId, "Transporte", "car", "#3357FF"),
                buildCategory(userId, "Lazer", "gamepad", "#FF33F5"),
                buildCategory(userId, "Saúde", "heartbeat", "#33FFF5"),
                buildCategory(userId, "Educação", "book", "#F5FF33")
        );

        categoryRepository.saveAll(defaultCategories);
    }

    @Transactional
    public CategoryResponse updateCategory(UUID userId, UUID categoryId, UpdateCategory request) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (!category.getName().equals(request.name()) &&
                categoryRepository.existsByUserIdAndName(userId, request.name())) {
            throw new ConflictException("Já existe uma categoria com o nome '" + request.name() + "'");
        }

        category.setName(request.name());
        category.setIcon(request.icon());
        category.setColor(request.color());

        Category updated = categoryRepository.save(category);

        log.info("Categoria '{}' atualizada - userId: {}", updated.getName(), userId);

        return categoryMapper.toDto(updated);
    }

    @Transactional
    public void deleteCategory(UUID userId, UUID categoryId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        categoryRepository.delete(category);
    }

    private Category buildCategory(UUID userId, String name, String icon, String color) {
        return Category.builder()
                .user(User.builder().id(userId).build())
                .name(name)
                .icon(icon)
                .color(color)
                .build();
    }
}
