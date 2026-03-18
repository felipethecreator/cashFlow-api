package com.cashflow.api.payment.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para criar um pagamento manualmente")
public record CreatePaymentRequest(

        @Schema(description = "ID da despesa", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Despesa é obrigatória")
        UUID expenseId,

        @Schema(description = "Mês de referência (sempre dia 1º)", example = "2025-03-01", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Mês de referência é obrigatório")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate referenceMonth,

        @Schema(description = "Valor a ser pago", example = "1200.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal amountPaid
) {
}
