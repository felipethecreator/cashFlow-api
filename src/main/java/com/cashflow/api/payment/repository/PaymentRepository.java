package com.cashflow.api.payment.repository;

import com.cashflow.api.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByExpenseId(UUID expenseId);

    @Query("""
        SELECT p FROM Payment p
        JOIN FETCH p.expense e
        JOIN FETCH e.category c
        WHERE e.user.id = :userId
          AND p.referenceMonth = :month
        ORDER BY e.dueDay ASC, e.name ASC
    """)
    List<Payment> findByUserAndMonth(@Param("userId") UUID userId, @Param("month") LocalDate month);

    Optional<Payment> findByExpenseIdAndReferenceMonth(UUID expenseId, LocalDate month);

    boolean existsByExpenseIdAndReferenceMonth(UUID expenseId, LocalDate month);

    @Query("""
        SELECT p FROM Payment p
        JOIN FETCH p.expense e
        JOIN FETCH e.category c
        WHERE p.paidAt IS NULL
          AND p.referenceMonth = :month
    """)
    List<Payment> findPendingByMonth(@Param("month") LocalDate month);

    @Query("""
        SELECT p FROM Payment p
        JOIN FETCH p.expense e
        JOIN FETCH e.category c
        WHERE p.notificationSent = false
          AND p.paidAt IS NULL
    """)
    List<Payment> findPendingNotifications();

    @Query("""
        SELECT p FROM Payment p
        JOIN FETCH p.expense e
        JOIN FETCH e.category c
        WHERE p.id = :paymentId
          AND e.user.id = :userId
    """)
    Optional<Payment> findByIdAndUserId(@Param("paymentId") UUID paymentId, @Param("userId") UUID userId);
}
