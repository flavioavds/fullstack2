# TaskList API - Documentação Técnica

## 📋 Visão Geral
API RESTful para gerenciamento de tarefas com sistema completo de autenticação e controle de sessão.

### **Informações Básicas**
- **Título:** Jtech Soluções em Informática
- **Versão:** v1
- **URL Base:** `http://localhost:8081/tasklist` (Desenvolvimento)

### **Autenticação**
- **Tipo:** JWT Bearer Token
- **Schema:** `bearerAuth`
- **Formato:** JWT

---

## 🔐 Endpoints de Autenticação

### **POST /api/v1/auth/register**
*Registro de novo usuário*

**Descrição:** Cadastra um novo usuário no sistema. Perfil e tipo de usuário são definidos automaticamente.

**Autenticação:** Não requer

**Request Body:**
```json
{
  "name": "Flavio Souza",
  "email": "flavio.souza@email.com",
  "password": "123456"
}
```

**Responses:**
- `201`: Usuário cadastrado com sucesso
- `400`: Dados inválidos
- `409`: Email já cadastrado
- `500`: Erro interno

### **POST /api/v1/auth/login**
*Autenticação de usuário*

**Descrição:** Autentica usuário e retorna tokens JWT.

**Autenticação:** Não requer

**Request Body:**
```json
{
  "email": "flavio.souza@email.com",
  "password": "123456"
}
```

**Responses:**
- `200`: Retorna access_token e refresh_token
- `400`: Requisição inválida
- `401`: Credenciais incorretas
- `500`: Erro interno

### **POST /api/v1/auth/logout**
*Logout do sistema*

**Descrição:** Invalida o token JWT atual.

**Autenticação:** Requer Bearer Token

**Responses:**
- `204`: Logout realizado
- `401`: Token inválido
- `500`: Erro interno

### **POST /api/v1/auth/refresh-token**
*Renovação de token*

**Descrição:** Gera novo access token usando refresh token.

**Autenticação:** Não documentada

---

## 👥 Endpoints de Usuários

### **GET /api/v1/user**
*Listar todos os usuários*

**Descrição:** Retorna todos usuários cadastrados (apenas ADMIN).

**Autenticação:** Requer Bearer Token + Permissão ADMIN

**Responses:**
- `200`: Lista de usuários
- `401`: Não autenticado
- `403`: Sem permissão
- `500`: Erro interno

### **GET /api/v1/user/me**
*Dados do usuário logado*

**Descrição:** Retorna dados do usuário autenticado.

**Autenticação:** Requer Bearer Token

**Responses:**
- `200`: Dados do usuário
- `401`: Não autenticado
- `500`: Erro interno

### **PUT /api/v1/user/me**
*Atualizar dados do usuário logado*

**Descrição:** Atualiza dados do próprio usuário.

**Autenticação:** Requer Bearer Token

**Request Body:**
```json
{
  "name": "Flavio Augusto Venancio de Souza",
  "email": "flavio.souza@gmail.com",
  "password": "Fla123"
}
```

**⚠️ Atenção:** Alteração de email/senha requer novo login.

**Responses:**
- `200`: Usuário atualizado
- `400`: Dados inválidos
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Usuário não encontrado
- `500`: Erro interno

### **GET /api/v1/user/{id}**
*Obter usuário por ID*

**Descrição:** Busca usuário específico pelo UUID.

**Autenticação:** Requer Bearer Token + Permissões

**Parâmetros:**
- `id` (path, required): UUID do usuário

**Responses:**
- `200`: Usuário encontrado
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Não encontrado
- `500`: Erro interno

### **PUT /api/v1/user/{id}**
*Atualizar usuário por ID*

**Descrição:** Atualiza dados de usuário específico.

**Autenticação:** Requer Bearer Token + Permissões

**⚠️ Atenção:** Apenas dono do token pode atualizar seus dados.

**Responses:**
- `200`: Usuário atualizado
- `400`: Dados inválidos
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Não encontrado
- `500`: Erro interno

### **DELETE /api/v1/user/{id}**
*Excluir usuário*

**Descrição:** Remove usuário do sistema.

**Autenticação:** Requer Bearer Token + Permissão ADMIN

**Responses:**
- `204`: Usuário excluído
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Não encontrado
- `500`: Erro interno

---

## 📝 Endpoints de TaskLists

### **GET /api/v1/tasklists**
*Listar todas as tasklists*

**Descrição:** Retorna todas as listas do usuário autenticado.

**Autenticação:** Requer Bearer Token

**Responses:**
- `200`: Array de tasklists
- `401`: Não autenticado
- `403`: Não autorizado
- `500`: Erro interno

### **POST /api/v1/tasklists**
*Criar nova tasklist*

**Descrição:** Cria nova lista de tarefas associada ao usuário.

**Autenticação:** Requer Bearer Token

**Request Body:**
```json
{
  "name": "Minhas tarefas diárias",
  "description": "Lista de tarefas pessoais"
}
```

**Responses:**
- `200`: Tasklist criada
- `400`: Dados inválidos
- `401`: Não autenticado
- `500`: Erro interno

### **GET /api/v1/tasklists/{id}**
*Consultar tasklist por ID*

**Descrição:** Retorna detalhes de tasklist específica.

**Autenticação:** Requer Bearer Token

**Parâmetros:**
- `id` (path, required): UUID da tasklist

**Responses:**
- `200`: Tasklist encontrada
- `400`: ID inválido
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Não encontrada
- `500`: Erro interno

### **PUT /api/v1/tasklists/{id}**
*Atualizar tasklist*

**Descrição:** Atualiza dados de tasklist existente.

**Autenticação:** Requer Bearer Token

**Request Body:**
```json
{
  "name": "Trabalho Atualizado",
  "description": "Tarefas do trabalho atualizadas"
}
```

**Responses:**
- `200`: Tasklist atualizada
- `400`: Dados inválidos
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Não encontrada
- `500`: Erro interno

### **DELETE /api/v1/tasklists/{id}**
*Excluir tasklist*

**Descrição:** Remove tasklist do sistema.

**Autenticação:** Requer Bearer Token

**Responses:**
- `204`: Tasklist excluída
- `400`: ID inválido
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Não encontrada
- `500`: Erro interno

### **PUT /api/v1/tasklists/{id}/complete**
*Marcar como concluída*

**Descrição:** Marca tasklist como concluída.

**Autenticação:** Requer Bearer Token

**Responses:**
- `200`: Tasklist concluída
- `400`: Dados inválidos
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Não encontrada
- `500`: Erro interno

---

## 📊 Schemas

### **UserRequest**
```json
{
  "id": "string (uuid)",
  "name": "string",
  "email": "string",
  "password": "string",
  "roles": ["string"],
  "userType": "string"
}
```

### **UserResponse**
```json
{
  "id": "string (uuid)",
  "name": "string",
  "email": "string"
}
```

### **TasklistRequest**
```json
{
  "id": "string (uuid)",
  "name": "string",
  "description": "string",
  "completed": "boolean",
  "userId": "string (uuid)",
  "requests": ["TasklistRequest"]
}
```

### **TasklistResponse**
```json
{
  "id": "string (uuid)",
  "name": "string",
  "description": "string",
  "completed": "boolean",
  "userId": "string (uuid)",
  "responses": ["TasklistResponse"]
}
```

### **AuthenticationRequest**
```json
{
  "email": "string",
  "password": "string"
}
```

### **AuthenticationResponse**
```json
{
  "access_token": "string",
  "refresh_token": "string"
}
```

---

## ⚠️ Observações Importantes

### **Regras de Segurança:**
1. Endpoints protegidos requerem JWT válido
2. Usuários só acessam seus próprios dados
3. Ações administrativas requerem permissão específica

### **Tokens:**
- Alteração de email/senha invalida tokens atuais
- É necessário novo login após estas alterações
- Use refresh-token para renovar access tokens

### **IDs:**
- Todos os IDs são no formato UUID
- Validação automática de formato

---

## 🔗 Ambientes Disponíveis

| Ambiente | URL Base | Descrição |
|----------|----------|-----------|
| Desenvolvimento | `http://localhost:8081/tasklist` | Ambiente local |

---

## 🚀 Começando

### **1. Registro**
```bash
curl -X POST "http://localhost:8081/tasklist/api/v1/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"name":"Seu Nome","email":"seu@email.com","password":"suaSenha"}'
```

### **2. Login**
```bash
curl -X POST "http://localhost:8081/tasklist/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"seu@email.com","password":"suaSenha"}'
```

### **3. Usar API (exemplo)**
```bash
curl -X GET "http://localhost:8081/tasklist/api/v1/tasklists" \
  -H "Authorization: Bearer SEU_JWT_TOKEN"
```

---

## 📞 Suporte

- **Empresa:** Jtech Soluções em Informática
- **Contato:** Helder Puia
- **Email:** helder.puia@veolia.com
- **Termos:** www.jtech.com.br/terms-and-condition

---

*Documentação gerada com base na especificação OpenAPI 3.1.0*