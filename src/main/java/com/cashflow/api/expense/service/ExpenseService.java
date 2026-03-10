package com.cashflow.api.expense.service;

import com.cashflow.api.category.entity.Category;
import com.cashflow.api.category.repository.CategoryRepository;
import com.cashflow.api.common.exceptions.NotFoundException;
import com.cashflow.api.expense.dto.input.CreateExpense;
import com.cashflow.api.expense.dto.input.UpdateExpense;
import com.cashflow.api.expense.dto.mapper.ExpenseMapper;
import com.cashflow.api.expense.dto.output.ExpenseResponse;
import com.cashflow.api.expense.entity.Expense;
import com.cashflow.api.expense.repository.ExpenseRepository;
import com.cashflow.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getUserExpenses(UUID userId) {
        return expenseRepository.findByUserId(userId).stream()
                .map(expenseMapper::toDto).toList();
    }

    @Transactional
    public ExpenseResponse createExpense(UUID userId, CreateExpense request) {
        Category category = categoryRepository.findByIdAndUserId(request.categoryId(), userId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        Expense expense = Expense.builder()
                .user(User.builder().id(userId).build())
                .category(category)
                .name(request.name())
                .amount(request.amount())
                .dueDay(request.dueDay())
                .priority(request.priority())
                .isActive(true)
                .isRecurring(request.isRecurring() != null ? request.isRecurring() : true)
                .build();

        Expense saved = expenseRepository.save(expense);
        log.info("Despesa '{}' criada - userId: {}, valor: {}",
                saved.getName(), userId, saved.getAmount());

        return expenseMapper.toDto(saved);
    }

    @Transactional
    public ExpenseResponse updateExpense(UUID userId, UUID expenseId, UpdateExpense request) {
        Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada"));

        Category category = categoryRepository.findByIdAndUserId(request.categoryId(), userId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        expense.setName(request.name());
        expense.setCategory(category);
        expense.setAmount(request.amount());
        expense.setDueDay(request.dueDay());
        expense.setPriority(request.priority());
        expense.setIsActive(request.isActive());
        expense.setIsRecurring(request.isRecurring());

        Expense updated = expenseRepository.save(expense);

        log.info("Despesa '{}' atualizada - userId: {}", updated.getName(), userId);

        return expenseMapper.toDto(updated);
    }

    @Transactional
    public void deleteExpense(UUID userId, UUID expenseId) {
        Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada"));

        expenseRepository.delete(expense);

        log.info("Despesa '{}' deletada - userId: {}", expense.getName(), userId);
    }
}
