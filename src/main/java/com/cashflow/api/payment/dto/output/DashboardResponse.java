package com.cashflow.api.payment.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Resumo financeiro do mês")
public record DashboardResponse(

        @Schema(description = "Mês de referência")
        LocalDate month,

        @Schema(description = "Total de despesas do mês")
        BigDecimal totalExpenses,

        @Schema(description = "Total já pago")
        BigDecimal totalPaid,

        @Schema(description = "Total pendente")
        BigDecimal totalPending,

        @Schema(description = "Total atrasado")
        BigDecimal totalOverdue,

        @Schema(description = "Quantidade total de pagamentos")
        int countTotal,

        @Schema(description = "Quantidade de pagamentos efetuados")
        int countPaid,

        @Schema(description = "Quantidade de pagamentos pendentes")
        int countPending,

        @Schema(description = "Quantidade de pagamentos atrasados")
        int countOverdue
) {
}
