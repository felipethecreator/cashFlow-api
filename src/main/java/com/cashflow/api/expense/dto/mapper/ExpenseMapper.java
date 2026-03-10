package com.cashflow.api.expense.dto.mapper;

import com.cashflow.api.expense.dto.input.CreateExpense;
import com.cashflow.api.expense.dto.output.ExpenseResponse;
import com.cashflow.api.expense.entity.Expense;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    ExpenseResponse toDto(Expense expense);

    Expense toEntity(CreateExpense createExpense);
}
