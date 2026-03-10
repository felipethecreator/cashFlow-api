CREATE TABLE expenses (
    id              UUID PRIMARY KEY,
    user_id         UUID NOT NULL,
    category_id     UUID NOT NULL,

    name            VARCHAR(255) NOT NULL,
    amount          DECIMAL(10, 2) NOT NULL,
    due_day         INTEGER NOT NULL,

    priority        VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    is_recurring    BOOLEAN NOT NULL DEFAULT TRUE,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_expense_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id)
        REFERENCES categories(id) ON DELETE RESTRICT,

    CONSTRAINT chk_expense_due_day CHECK (due_day >= 1 AND due_day <= 31),
    CONSTRAINT chk_expense_amount CHECK (amount > 0),
    CONSTRAINT chk_expense_priority CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW'))
);

CREATE INDEX idx_expenses_user ON expenses(user_id);
CREATE INDEX idx_expenses_category ON expenses(category_id);
CREATE INDEX idx_expenses_active ON expenses(user_id, is_active) WHERE is_active = TRUE;
CREATE INDEX idx_expenses_due_day ON expenses(due_day);

COMMENT ON TABLE expenses IS 'Despesas recorrentes do usuário';
COMMENT ON COLUMN expenses.due_day IS 'Dia do mês em que a despesa vence (1-31)';
COMMENT ON COLUMN expenses.priority IS 'HIGH, MEDIUM, LOW';
COMMENT ON COLUMN expenses.is_active IS 'Se FALSE, não gera pagamentos';
COMMENT ON COLUMN expenses.is_recurring IS 'Se TRUE, gera pagamento todo mês';