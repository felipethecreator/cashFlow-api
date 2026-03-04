package com.cashflow.api.category.dto.mapper;

import com.cashflow.api.category.dto.input.CreateCategory;
import com.cashflow.api.category.dto.output.CategoryResponse;
import com.cashflow.api.category.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toDto(Category category);

    Category toEntity(CreateCategory createCategory);
}
