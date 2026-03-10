package com.cashflow.api.expense.controller;

import com.cashflow.api.expense.dto.input.CreateExpense;
import com.cashflow.api.expense.dto.input.UpdateExpense;
import com.cashflow.api.expense.dto.output.ExpenseResponse;
import com.cashflow.api.expense.service.ExpenseService;
import com.cashflow.api.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expenseService.getUserExpenses(user.getId()));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateExpense request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(expenseService.createExpense(user.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateExpense request
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(user.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        expenseService.deleteExpense(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
