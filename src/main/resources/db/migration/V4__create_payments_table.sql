CREATE TABLE payments (
    id                      UUID PRIMARY KEY,
    expense_id              UUID NOT NULL,

    reference_month         DATE NOT NULL,
    paid_at                 TIMESTAMP,
    amount_paid             DECIMAL(10, 2) NOT NULL,
    notes                   TEXT,

    notification_sent       BOOLEAN NOT NULL DEFAULT FALSE,
    notification_sent_at    TIMESTAMP,

    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_expense FOREIGN KEY (expense_id)
        REFERENCES expenses(id) ON DELETE CASCADE,
    CONSTRAINT uk_payment_expense_month UNIQUE (expense_id, reference_month),
    CONSTRAINT chk_payment_amount CHECK (amount_paid > 0)
);

CREATE INDEX idx_payments_expense ON payments(expense_id);
CREATE INDEX idx_payments_reference_month ON payments(reference_month);
CREATE INDEX idx_payments_paid_at ON payments(paid_at);

CREATE INDEX idx_payments_pending ON payments(reference_month, paid_at)
    WHERE paid_at IS NULL;

CREATE INDEX idx_payments_notification_pending ON payments(notification_sent)
    WHERE notification_sent = FALSE;

COMMENT ON TABLE payments IS 'Pagamentos mensais de cada despesa';
COMMENT ON COLUMN payments.reference_month IS 'Mês de referência (ex: 2025-03-01 = março/2025)';
COMMENT ON COLUMN payments.paid_at IS 'Data/hora em que foi marcado como pago (NULL = pendente)';
COMMENT ON COLUMN payments.amount_paid IS 'Valor efetivamente pago (pode ser diferente da despesa)';
COMMENT ON COLUMN payments.notification_sent IS 'Flag para workers: notificação de vencimento enviada?';
