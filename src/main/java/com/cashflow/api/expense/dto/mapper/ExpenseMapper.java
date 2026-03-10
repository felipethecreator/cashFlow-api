package com.cashflow.api.expense.dto.mapper;

import com.cashflow.api.expense.dto.input.CreateExpense;
import com.cashflow.api.expense.dto.output.ExpenseResponse;
import com.cashflow.api.expense.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "categoryId", source = "category.id")
    ExpenseResponse toDto(Expense expense);

    Expense toEntity(CreateExpense createExpense);
}
