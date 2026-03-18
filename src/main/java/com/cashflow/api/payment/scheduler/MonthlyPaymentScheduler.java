package com.cashflow.api.payment.scheduler;

import com.cashflow.api.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class MonthlyPaymentScheduler {

    private final PaymentService paymentService;

    @Scheduled(cron = "${app.payments.scheduler.cron:0 0 2 1 * *}")
    public void generateMonthlyPayments() {
        LocalDate month = LocalDate.now().withDayOfMonth(1);
        int created = paymentService.generateMonthlyPaymentsForAllUsers(month);

        log.info("Scheduler de pagamentos executado. month={}, created={}", month, created);
    }
}
