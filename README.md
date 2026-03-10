# 💰 CashFlow API

> Sistema de Controle Financeiro Pessoal com notificações inteligentes e gerenciamento de despesas recorrentes.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

---

## 📋 Índice

- [Sobre](#sobre)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando](#executando)
- [Documentação da API](#documentação-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Roadmap](#roadmap)
- [Contribuindo](#contribuindo)
- [Licença](#licença)
- [Autor](#autor)

---

## 📖 Sobre

O **CashFlow API** é um sistema completo de controle financeiro pessoal desenvolvido com Spring Boot, focado em gerenciamento de despesas recorrentes, categorização inteligente e notificações automáticas de vencimento.

### Problema que resolve

- ❌ Dificuldade em acompanhar despesas mensais recorrentes
- ❌ Esquecimento de vencimentos e atrasos
- ❌ Falta de visibilidade sobre gastos por categoria
- ❌ Controle financeiro manual e desorganizado

### Solução

- ✅ Cadastro de despesas com vencimento automático mensal
- ✅ Categorização personalizável de gastos
- ✅ Notificações inteligentes de vencimento (futuramente)
- ✅ Dashboard com visão consolidada dos gastos
- ✅ API RESTful documentada e segura

---

## ⚡ Funcionalidades

### ✅ Implementado

- **Autenticação & Autorização**
  - Registro de usuários com validação
  - Login com JWT (JSON Web Token)
  - Proteção de rotas com Spring Security
  - CORS configurado para frontend

- **Gerenciamento de Categorias**
  - CRUD completo de categorias
  - Categorias padrão criadas automaticamente no registro
  - Validação de duplicatas
  - Ícones e cores personalizáveis
  - Proteção contra deleção de categorias em uso

- **Gerenciamento de Despesas**
  - CRUD completo de despesas recorrentes
  - Valores monetários com precisão decimal (BigDecimal)
  - Priorização de despesas (HIGH, MEDIUM, LOW)
  - Controle de ativo/inativo
  - Despesas recorrentes ou únicas
  - Validação de integridade com categorias

### 🚧 Em Desenvolvimento

- **Pagamentos Mensais**
  - Geração automática de pagamentos mensais
  - Controle de status (pago/pendente)
  - Dashboard financeiro

- **Notificações Inteligentes** (via RabbitMQ)
  - Lembretes 3 dias antes do vencimento
  - Notificação no dia do vencimento
  - Alertas de atraso

- **Relatórios**
  - Exportação em PDF/CSV
  - Processamento assíncrono via filas

---

## 🛠 Tecnologias

### Backend

- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.3** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **PostgreSQL 16** - Banco de dados relacional
- **Flyway** - Versionamento de banco de dados
- **Lombok** - Redução de boilerplate
- **Bean Validation** - Validação de dados
- **JWT (java-jwt)** - Tokens de autenticação

### Documentação

- **Swagger/OpenAPI 3** - Documentação interativa da API

### Futuras

- **RabbitMQ** - Sistema de mensageria
- **MailHog** - Servidor SMTP para desenvolvimento
- **Docker** - Containerização

### Ferramentas de Desenvolvimento

- **Maven** - Gerenciamento de dependências
- **Docker Compose** - Orquestração de containers
- **IntelliJ IDEA** - IDE

---

## 🏗 Arquitetura

### Padrões Utilizados

- **Feature-based Package Structure** - Organização por funcionalidade
- **DTO Pattern** - Separação de DTOs de entrada e saída
- **Repository Pattern** - Abstração de acesso a dados
- **Service Layer** - Lógica de negócio centralizada
- **Mapper Pattern** - Conversão entre entities e DTOs
- **Builder Pattern** - Construção fluente de objetos
- **Exception Handling Global** - Tratamento centralizado de erros

### Camadas
```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← REST endpoints
├─────────────────────────────────────┤
│          Service Layer              │  ← Lógica de negócio
├─────────────────────────────────────┤
│        Repository Layer             │  ← Acesso a dados
├─────────────────────────────────────┤
│         Database (PostgreSQL)       │  ← Persistência
└─────────────────────────────────────┘
```

### Modelo de Dados (Atual)
```
┌──────────┐
│   USER   │
└─────┬────┘
      │ 1:N
      ├─────────────────────────┐
      │                         │
      ▼ N                       ▼ N
┌────────────┐           ┌──────────┐
│  CATEGORY  │           │ EXPENSE  │
└─────┬──────┘           └──────────┘
      │                        
      │ 1:N                    
      └────────────────────────┘
```

---

## ✅ Pré-requisitos

Antes de começar, você precisa ter instalado:

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8+](https://maven.apache.org/)
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)
- [Git](https://git-scm.com/)

---

## 🚀 Instalação

### 1. Clone o repositório
```bash
git clone https://github.com/felipethecreator/cashFlow-api.git
cd cashFlow-api
```

### 2. Suba os containers Docker
```bash
docker-compose up -d
```

Isso iniciará:
- PostgreSQL (porta 5433)
- RabbitMQ (portas 5672, 15672)
- MailHog (portas 1025, 8025)

### 3. Verifique se os containers estão rodando
```bash
docker ps
```

Você deve ver:
```
cashflow-postgres
cashflow-rabbitmq
cashflow-mailhog
```

---

## ⚙️ Configuração

### Variáveis de Ambiente (Opcionais)

Você pode sobrescrever as configurações padrão criando um arquivo `application-local.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cashflow
    username: cashflow
    password: cashflow123

jwt:
  secret: sua-chave-super-secreta-aqui
  expiration: 86400000  # 24 horas
```

---

## ▶️ Executando

### Via Maven
```bash
./mvnw spring-boot:run
```

### Via JAR (Produção)
```bash
./mvnw clean package
java -jar target/cashflow-api-1.0.0.jar
```

A aplicação estará disponível em: **http://localhost:8080**

---

## 📚 Documentação da API

### Swagger UI (Recomendado)

Acesse: **http://localhost:8080/swagger-ui.html**

Interface interativa para testar todos os endpoints da API.

### Endpoints Principais

#### Autenticação
```http
POST /api/auth/register
POST /api/auth/login
```

#### Categorias
```http
GET    /api/categories
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}
```

#### Despesas
```http
GET    /api/expenses
POST   /api/expenses
PUT    /api/expenses/{id}
DELETE /api/expenses/{id}
```

### Exemplos de Requisições

#### Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

#### Criar Despesa
```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{
    "name": "Aluguel",
    "categoryId": "uuid-da-categoria",
    "amount": 1200.00,
    "dueDay": 5,
    "priority": "HIGH",
    "isRecurring": true
  }'
```

---

## 📁 Estrutura do Projeto
```
cashflow-api/
├── src/
│   ├── main/
│   │   ├── java/com/cashflow/api/
│   │   │   ├── category/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   │   ├── input/
│   │   │   │   │   └── output/
│   │   │   │   ├── entity/
│   │   │   │   ├── mapper/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   ├── expense/
│   │   │   │   └── (mesma estrutura)
│   │   │   ├── user/
│   │   │   │   └── (mesma estrutura)
│   │   │   ├── config/
│   │   │   │   └── jwt/
│   │   │   ├── shared/
│   │   │   │   ├── errors/
│   │   │   │   └── exceptions/
│   │   │   └── CashFlowApiApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   ├── V1__create_users_table.sql
│   │       │   ├── V2__create_categories_table.sql
│   │       │   └── V3__create_expenses_table.sql
│   │       └── application.yml
│   └── test/ (em desenvolvimento)
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🗺 Roadmap

### ✅ Fase 1 - Core (Completo)
- [x] Autenticação JWT
- [x] CRUD de Categorias
- [x] CRUD de Despesas
- [x] Validações e Exception Handling
- [x] Documentação Swagger

### 🚧 Fase 2 - Pagamentos (Em Desenvolvimento)
- [ ] Entity Payment
- [ ] CRUD de Pagamentos
- [ ] Marcar como pago/pendente
- [ ] Dashboard financeiro

### 📅 Fase 3 - Automação
- [ ] Scheduled Job (geração automática de pagamentos)
- [ ] RabbitMQ (sistema de filas)
- [ ] Notificações por email

### 📊 Fase 4 - Relatórios
- [ ] Exportação em PDF
- [ ] Exportação em CSV
- [ ] Processamento assíncrono

### 🧪 Fase 5 - Qualidade
- [ ] Testes unitários
- [ ] Testes de integração
- [ ] Cobertura de testes >80%

---

## 🤝 Contribuindo

Contribuições são sempre bem-vindas!

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Add: nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 👨‍💻 Autor

**Felipe Rodrigues Queiroz**

- GitHub: [@felipethecreator](https://github.com/felipethecreator)
- LinkedIn: [Felipe Rodrigues Queiroz]([https://www.linkedin.com/in/seu-perfil](https://www.linkedin.com/in/felipe-rodrigues-queiroz-564377171/))
- Email: felipinhodev@gmail.com
