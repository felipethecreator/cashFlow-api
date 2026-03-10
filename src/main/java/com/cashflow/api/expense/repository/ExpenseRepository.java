package com.cashflow.api.expense.repository;

import com.cashflow.api.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByUserIdAndIsActiveTrue(UUID userId);

    List<Expense> findByUserId(UUID userId);

    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);

    long countByUserId(UUID userId);

    long countByCategoryId(UUID categoryId);
}
