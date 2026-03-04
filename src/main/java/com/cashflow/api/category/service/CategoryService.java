package com.cashflow.api.category.service;

import com.cashflow.api.category.dto.input.CreateCategory;
import com.cashflow.api.category.dto.mapper.CategoryMapper;
import com.cashflow.api.category.dto.output.CategoryResponse;
import com.cashflow.api.category.entity.Category;
import com.cashflow.api.category.repository.CategoryRepository;
import com.cashflow.api.common.exceptions.UnauthorizedException;
import com.cashflow.api.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
