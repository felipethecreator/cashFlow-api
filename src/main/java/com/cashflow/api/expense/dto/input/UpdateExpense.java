package com.cashflow.api.expense.dto.input;

import com.cashflow.api.expense.entity.ExpensePriority;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateExpense(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255)
        String name,

        @NotNull(message = "Categoria é obrigatória")
        UUID categoryId,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01")
        @Digits(integer = 8, fraction = 2)
        BigDecimal amount,

        @NotNull(message = "Dia de vencimento é obrigatório")
        @Min(1) @Max(31)
        Integer dueDay,

        @NotNull(message = "Prioridade é obrigatória")
        ExpensePriority priority,

        @NotNull(message = "Status ativo é obrigatório")
        Boolean isActive,

        @NotNull(message = "Recorrência é obrigatória")
        Boolean isRecurring
) {
}
