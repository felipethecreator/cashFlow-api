CREATE TABLE categories (
    id              UUID PRIMARY KEY,
    user_id         UUID NOT NULL,
    name            VARCHAR(100) NOT NULL,
    icon            VARCHAR(50),
    color           VARCHAR(7),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_category_user FOREIGN KEY (user_id)
                        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_category_user_name UNIQUE (user_id, name)
);

CREATE INDEX idx_categories_user ON categories(user_id);

COMMENT ON TABLE categories IS 'Categorias de despesas personalizadas por usuário';
COMMENT ON COLUMN categories.icon IS 'Nome do ícone para UI (ex: home, car, food)';
COMMENT ON COLUMN categories.color IS 'Cor em HEX (ex: #FF5733)';