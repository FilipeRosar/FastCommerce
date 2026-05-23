# FastCommerce
Exercicio de Desafio Tecnico - Java

## 🚀 Como Executar com Docker

### Pré-requisitos
- Docker e Docker Compose instalados

### Passos para executar

1. **Clone o repositório** (se necessário)
```bash
git clone <seu-repositorio>
cd FastCommerce
```

2. **Configure as variáveis de ambiente**
```bash
# Copie o arquivo de exemplo (já vem configurado com valores padrão)
cp .env.example .env

# Opcionalmente, edite o arquivo .env para alterar as credenciais
# (PostgreSQL, PGAdmin, RabbitMQ, Redis, etc)
```

3. **Inicie os containers**
```bash
docker-compose up -d
```

O Docker irá:
- ✅ Buildar a aplicação Spring Boot automaticamente
- ✅ Iniciar PostgreSQL (porta 5433)
- ✅ Iniciar Redis (porta 6379)
- ✅ Iniciar RabbitMQ (porta 5672, dashboard em 15672)
- ✅ Iniciar PGAdmin (porta 5050)
- ✅ Iniciar a aplicação Spring Boot (porta 8080)

4. **Verifique o status**
```bash
docker-compose ps
```

### Acessar os serviços

- **Aplicação**: http://localhost:8080
- **PGAdmin**: http://localhost:5050 (admin@admin.com / root)
- **RabbitMQ Dashboard**: http://localhost:15672 (guest / guest)

### Parar os containers

```bash
docker-compose down
```

Para remover volumes de dados também:
```bash
docker-compose down -v
```

### Variáveis de Ambiente Disponíveis

Edite o arquivo `.env` para customizar:

- `POSTGRES_USER` - Usuário do PostgreSQL
- `POSTGRES_PASSWORD` - Senha do PostgreSQL
- `POSTGRES_DB` - Nome do banco de dados
- `POSTGRES_PORT` - Porta do PostgreSQL (padrão: 5433)
- `PGADMIN_EMAIL` - Email do PGAdmin
- `PGADMIN_PASSWORD` - Senha do PGAdmin
- `REDIS_PORT` - Porta do Redis
- `RABBITMQ_USER` - Usuário do RabbitMQ
- `RABBITMQ_PASSWORD` - Senha do RabbitMQ
- `APP_PORT` - Porta da aplicação Spring Boot
