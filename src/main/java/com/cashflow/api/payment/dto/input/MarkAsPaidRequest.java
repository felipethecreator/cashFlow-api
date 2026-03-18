package com.cashflow.api.payment.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para marcar pagamento como pago")
public record MarkAsPaidRequest(

        @Schema(description = "Observações sobre o pagamento (opcional)", example = "Pago via PIX")
        @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
        String notes
) {
}
