CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    password_hash       VARCHAR(255) NOT NULL,
    role                VARCHAR(50) NOT NULL DEFAULT 'PARTICIPANT',
    profile_photo_id    UUID,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'ORGANIZER', 'PARTICIPANT')),
    CONSTRAINT uk_user_email UNIQUE (email)
);

COMMENT ON TABLE users IS 'Usuários do sistema (participantes, organizadores e administradores)';
COMMENT ON COLUMN users.role IS 'Tipo de usuário: ADMIN (administrador geral), ORGANIZER (organizador de eventos), PARTICIPANT (participante)';
COMMENT ON COLUMN users.profile_photo_id IS 'FK para files - foto de perfil do usuário';

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);