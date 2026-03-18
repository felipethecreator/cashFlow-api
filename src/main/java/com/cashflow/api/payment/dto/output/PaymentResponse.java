package com.cashflow.api.payment.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de um pagamento")
public record PaymentResponse(

        @Schema(description = "ID do pagamento")
        UUID id,

        @Schema(description = "ID da despesa")
        UUID expenseId,

        @Schema(description = "Nome da despesa", example = "Aluguel")
        String expenseName,

        @Schema(description = "Categoria da despesa", example = "Moradia")
        String categoryName,

        @Schema(description = "Mês de referência", example = "2025-03-01")
        LocalDate referenceMonth,

        @Schema(description = "Data/hora em que foi pago (null = pendente)")
        LocalDateTime paidAt,

        @Schema(description = "Valor pago", example = "1200.00")
        BigDecimal amountPaid,

        @Schema(description = "Observações")
        String notes,

        @Schema(description = "Se está pago")
        Boolean isPaid,

        @Schema(description = "Dia de vencimento", example = "5")
        Integer dueDay,

        @Schema(description = "Data de criação")
        LocalDateTime createdAt
) {
}
