package com.cashflow.api.payment.entity;

import com.cashflow.api.expense.entity.Expense;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"expense_id", "reference_month"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    @JsonIgnore
    private Expense expense;

    @Column(name = "reference_month", nullable = false)
    private LocalDate referenceMonth;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "amount_paid", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    @Column(name = "notification_sent", nullable = false)
    private Boolean notificationSent = false;

    @Column(name = "notification_sent_at")
    private LocalDateTime notificationSentAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public boolean isPaid() {
        return paidAt != null;
    }

    public boolean isPending() {
        return paidAt == null;
    }

    public boolean isOverdue(LocalDate today) {
        if (isPaid()) {
            return false;
        }

        int dueDay = expense.getDueDay();
        LocalDate dueDate = referenceMonth.withDayOfMonth(
                Math.min(dueDay, referenceMonth.lengthOfMonth())
        );

        return today.isAfter(dueDate);
    }
}
