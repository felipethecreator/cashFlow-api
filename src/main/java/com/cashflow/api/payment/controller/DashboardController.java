package com.cashflow.api.payment.controller;

import com.cashflow.api.common.exceptions.BadRequestException;
import com.cashflow.api.common.security.AuthenticatedUser;
import com.cashflow.api.payment.dto.output.DashboardResponse;
import com.cashflow.api.payment.service.DashboardService;
import com.cashflow.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping({"/api/dashboard", "/dashboard"})
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> getSummary(
            Authentication authentication,
            @RequestParam(required = false) String month
    ) {
        User user = AuthenticatedUser.require(authentication);
        LocalDate parsedMonth = parseMonth(month);
        return ResponseEntity.ok(dashboardService.getMonthlySummary(user.getId(), parsedMonth));
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
