package com.cashflow.api.expense.dto.output;

import com.cashflow.api.expense.entity.ExpensePriority;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        String name,
        String categoryName,
        UUID categoryId,
        BigDecimal amount,
        Integer dueDay,
        ExpensePriority priority,
        Boolean isActive,
        Boolean isRecurring,
        LocalDateTime createdAt
) {
}
