package com.cashflow.api.payment.service;

import com.cashflow.api.common.exceptions.ConflictException;
import com.cashflow.api.common.exceptions.NotFoundException;
import com.cashflow.api.expense.entity.Expense;
import com.cashflow.api.expense.repository.ExpenseRepository;
import com.cashflow.api.payment.dto.input.CreatePaymentRequest;
import com.cashflow.api.payment.dto.input.MarkAsPaidRequest;
import com.cashflow.api.payment.dto.output.PaymentResponse;
import com.cashflow.api.payment.entity.Payment;
import com.cashflow.api.payment.mapper.PaymentMapper;
import com.cashflow.api.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public List<PaymentResponse> getUserPaymentsByMonth(UUID userId, LocalDate month) {
        LocalDate normalizedMonth = normalizeMonth(month);
        ensureMonthlyPaymentsForUser(userId, normalizedMonth);

        return paymentRepository.findByUserAndMonth(userId, normalizedMonth)
                .stream()
                .map(PaymentMapper::toDto)
                .toList();
    }

    @Transactional
    public PaymentResponse createManualPayment(UUID userId, CreatePaymentRequest request) {
        Expense expense = expenseRepository.findByIdAndUserId(request.expenseId(), userId)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada"));

        LocalDate normalizedMonth = normalizeMonth(request.referenceMonth());

        if (paymentRepository.existsByExpenseIdAndReferenceMonth(expense.getId(), normalizedMonth)) {
            throw new ConflictException("Já existe pagamento para esta despesa no mês informado");
        }

        Payment payment = Payment.builder()
                .expense(expense)
                .referenceMonth(normalizedMonth)
                .amountPaid(request.amountPaid())
                .build();

        Payment saved = paymentRepository.save(payment);

        log.info("Pagamento criado manualmente. paymentId={}, expenseId={}, month={}",
                saved.getId(), expense.getId(), normalizedMonth);

        return PaymentMapper.toDto(saved);
    }

    @Transactional
    public PaymentResponse markAsPaid(UUID userId, UUID paymentId, MarkAsPaidRequest request) {
        Payment payment = paymentRepository.findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado"));

        payment.setPaidAt(LocalDateTime.now());
        payment.setNotes(normalizeNotes(request != null ? request.notes() : null));

        Payment saved = paymentRepository.save(payment);

        log.info("Pagamento marcado como pago. paymentId={}, userId={}", paymentId, userId);

        return PaymentMapper.toDto(saved);
    }

    @Transactional
    public PaymentResponse markAsPending(UUID userId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado"));

        payment.setPaidAt(null);

        Payment saved = paymentRepository.save(payment);

        log.info("Pagamento marcado como pendente. paymentId={}, userId={}", paymentId, userId);

        return PaymentMapper.toDto(saved);
    }

    @Transactional
    public int generateMonthlyPaymentsForAllUsers(LocalDate month) {
        LocalDate normalizedMonth = normalizeMonth(month);
        List<Expense> expenses = expenseRepository.findByIsActiveTrueAndIsRecurringTrue();

        int created = 0;
        for (Expense expense : expenses) {
            if (createMonthlyPaymentIfMissing(expense, normalizedMonth)) {
                created++;
            }
        }

        if (created > 0) {
            log.info("Pagamentos mensais gerados. month={}, created={}", normalizedMonth, created);
        }

        return created;
    }

    @Transactional
    public int ensureMonthlyPaymentsForUser(UUID userId, LocalDate month) {
        LocalDate normalizedMonth = normalizeMonth(month);
        List<Expense> expenses = expenseRepository.findByUserIdAndIsActiveTrueAndIsRecurringTrue(userId);

        int created = 0;
        for (Expense expense : expenses) {
            if (createMonthlyPaymentIfMissing(expense, normalizedMonth)) {
                created++;
            }
        }

        return created;
    }

    public LocalDate normalizeMonth(LocalDate month) {
        LocalDate base = month != null ? month : LocalDate.now();
        return base.withDayOfMonth(1);
    }

    private boolean createMonthlyPaymentIfMissing(Expense expense, LocalDate month) {
        if (paymentRepository.existsByExpenseIdAndReferenceMonth(expense.getId(), month)) {
            return false;
        }

        Payment payment = Payment.builder()
                .expense(expense)
                .referenceMonth(month)
                .amountPaid(expense.getAmount())
                .build();

        try {
            paymentRepository.save(payment);
            return true;
        } catch (DataIntegrityViolationException ex) {
            // Evita falha em chamadas concorrentes (scheduler + requisição do usuário)
            return false;
        }
    }

    private String normalizeNotes(String notes) {
        if (notes == null) {
            return null;
        }

        String trimmed = notes.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}


