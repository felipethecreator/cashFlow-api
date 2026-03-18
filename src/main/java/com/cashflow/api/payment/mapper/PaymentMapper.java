package com.cashflow.api.payment.mapper;

import com.cashflow.api.payment.dto.output.PaymentResponse;
import com.cashflow.api.payment.entity.Payment;

public class PaymentMapper {

    private PaymentMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PaymentResponse toDto(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getExpense().getId(),
                payment.getExpense().getName(),
                payment.getExpense().getCategory().getName(),
                payment.getReferenceMonth(),
                payment.getPaidAt(),
                payment.getAmountPaid(),
                payment.getNotes(),
                payment.isPaid(),
                payment.getExpense().getDueDay(),
                payment.getCreatedAt()
        );
    }
}
