package com.cashflow.api.payment.controller;

import com.cashflow.api.common.exceptions.BadRequestException;
import com.cashflow.api.payment.dto.input.CreatePaymentRequest;
import com.cashflow.api.payment.dto.input.MarkAsPaidRequest;
import com.cashflow.api.payment.dto.output.PaymentResponse;
import com.cashflow.api.payment.service.PaymentService;
import com.cashflow.api.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/payments"})
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPaymentsByMonth(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String month
    ) {
        LocalDate parsedMonth = parseMonth(month);
        return ResponseEntity.ok(paymentService.getUserPaymentsByMonth(user.getId(), parsedMonth));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createManualPayment(user.getId(), request));
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<PaymentResponse> markAsPaid(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @RequestBody(required = false) MarkAsPaidRequest request
    ) {
        return ResponseEntity.ok(paymentService.markAsPaid(user.getId(), id, request));
    }

    @PatchMapping("/{id}/pending")
    public ResponseEntity<PaymentResponse> markAsPending(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(paymentService.markAsPending(user.getId(), id));
    }

    private LocalDate parseMonth(String rawMonth) {
        if (rawMonth == null || rawMonth.isBlank()) {
            return null;
        }

        String month = rawMonth.trim();

        try {
            if (month.matches("\\d{4}-\\d{2}")) {
                return YearMonth.parse(month).atDay(1);
            }

            return LocalDate.parse(month).withDayOfMonth(1);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Parâmetro 'month' inválido. Use yyyy-MM ou yyyy-MM-dd");
        }
    }
}
