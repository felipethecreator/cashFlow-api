CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id                  UUID PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    password_hash       VARCHAR(255) NOT NULL,
    profile_photo_id    UUID,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_user_email UNIQUE (email)
);

COMMENT ON TABLE users IS 'Usuários do sistema (participantes, organizadores e administradores)';
COMMENT ON COLUMN users.profile_photo_id IS 'FK para files - foto de perfil do usuário';

CREATE INDEX idx_users_email ON users(email);