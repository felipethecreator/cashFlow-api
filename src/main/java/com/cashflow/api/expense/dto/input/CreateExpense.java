package com.cashflow.api.expense.dto.input;

import com.cashflow.api.expense.entity.ExpensePriority;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateExpense(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        String name,

        @NotNull(message = "Categoria é obrigatória")
        UUID categoryId,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal amount,

        @NotNull(message = "Dia de vencimento é obrigatório")
        @Min(value = 1, message = "Dia deve ser entre 1 e 31")
        @Max(value = 31, message = "Dia deve ser entre 1 e 31")
        Integer dueDay,

        @NotNull(message = "Prioridade é obrigatória")
        ExpensePriority priority,

        Boolean isRecurring
) {

}
