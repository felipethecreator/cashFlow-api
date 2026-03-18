package com.cashflow.api.payment.service;

import com.cashflow.api.payment.dto.output.DashboardResponse;
import com.cashflow.api.payment.entity.Payment;
import com.cashflow.api.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @Transactional
    public DashboardResponse getMonthlySummary(UUID userId, LocalDate month) {
        LocalDate normalizedMonth = paymentService.normalizeMonth(month);
        paymentService.ensureMonthlyPaymentsForUser(userId, normalizedMonth);

        List<Payment> payments = paymentRepository.findByUserAndMonth(userId, normalizedMonth);

        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalPending = BigDecimal.ZERO;
        BigDecimal totalOverdue = BigDecimal.ZERO;

        int countPaid = 0;
        int countPending = 0;
        int countOverdue = 0;

        LocalDate today = LocalDate.now();

        for (Payment payment : payments) {
            BigDecimal amount = payment.getAmountPaid();
            totalExpenses = totalExpenses.add(amount);

            if (payment.isPaid()) {
                totalPaid = totalPaid.add(amount);
                countPaid++;
                continue;
            }

            LocalDate dueDate = buildDueDate(normalizedMonth, payment.getExpense().getDueDay());
            if (today.isAfter(dueDate)) {
                totalOverdue = totalOverdue.add(amount);
                countOverdue++;
            } else {
                totalPending = totalPending.add(amount);
                countPending++;
            }
        }

        return new DashboardResponse(
                normalizedMonth,
                totalExpenses,
                totalPaid,
                totalPending,
                totalOverdue,
                payments.size(),
                countPaid,
                countPending,
                countOverdue
        );
    }

    private LocalDate buildDueDate(LocalDate month, Integer dueDay) {
        int safeDueDay = dueDay == null ? 1 : dueDay;
        return month.withDayOfMonth(Math.min(Math.max(safeDueDay, 1), month.lengthOfMonth()));
    }
}
