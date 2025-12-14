# Desafio Técnico Fullstack - JTech
**Candidato:** Flavio Augusto Venancio de Souza  
**Email:** suporte.flavio.souza@hotmail.com  
**GitHub:** [https://github.com/flavioavds/fullstack2](https://github.com/flavioavds/fullstack2)

---

## 📋 Visão Geral do Projeto

Esta é uma aplicação completa **Fullstack** desenvolvida como parte do processo seletivo da JTech. O projeto consiste em uma API RESTful para gerenciamento de tarefas (TaskList) com sistema completo de autenticação e um front-end em Vue.js.

### **Arquitetura e Tecnologias**

| Componente | Tecnologia | Detalhes |
|------------|------------|----------|
| **Back-end** | Java 21 + Spring Boot | Arquitetura Hexagonal, Gradle |
| **Front-end** | Vue.js | Interface responsiva e intuitiva |
| **Banco de Dados** | H2 (desenvolvimento) | Schema: `sansys_database` |
| **Documentação** | Swagger UI | OpenAPI 3.1.0 |
| **Autenticação** | JWT (JSON Web Tokens) | Bearer token com refresh |

### **Status do Projeto**
✅ **Concluído e Funcional**  
✅ **Back-end com API REST completa**  
✅ **Front-end Vue.js responsivo**  
✅ **Documentação Swagger integrada**  
✅ **Tratamento de erros e validações**  
✅ **Configurações CORS e segurança**

---

## 🚀 Como Executar o Projeto

### **Pré-requisitos**
- Java 21
- Node.js 18+
- Gradle 8+
- NPM ou Yarn

### **Passo 1: Clonar e Configurar**
```bash
# Clonar repositório
git clone https://github.com/flavioavds/fullstack2.git
cd fullstack2

# Configurar ambiente Java
# Verificar se Java 21 está instalado
java -version

# Configurar variáveis de ambiente (se necessário)
```

### **Passo 2: Configurar Back-end**
```bash
# Navegar para diretório do back-end
# O projeto usa Gradle wrapper, então não precisa instalar Gradle separadamente

# Verificar e atualizar dependências
./gradlew dependencies

# Compilar o projeto
./gradlew build

# Executar a aplicação Spring Boot
./gradlew bootRun
```

**Configurações do Back-end:**
- **Porta:** 8081
- **Context Path:** `/tasklist`
- **Banco de Dados:** H2 (em memória)
- **Swagger UI:** Disponível após iniciar
- **Auto-update do schema:** Habilitado

### **Passo 3: Configurar Front-end**
```bash
# Navegar para diretório do front-end (presumo que esteja em /frontend)
cd frontend

# Instalar dependências
npm install
# ou
yarn install

# Executar servidor de desenvolvimento
npm run serve
# ou
yarn serve
```

**Configurações do Front-end:**
- **Porta:** 5173 (ou conforme configuração)
- **API Front-end:** `http://localhost:5173`
- **Proxy API:** `VITE_API_BASE_URL=http://localhost:8081/tasklist/api/v1`
- **Hot Reload:** Habilitado

### **Passo 4: Acessar a Aplicação**
1. **API Back-end:** `http://localhost:8081/tasklist`
2. **Swagger Documentation:** `http://localhost:8081/tasklist/doc/tasklist/v1/swagger-ui/index.html`
3. **Front-end Vue.js:** `http://localhost:5173`
4. **Banco Postgres:** - O mesmo do cadastrado no aplication.yml sansys_database (Já configurado para criar o banco de dados)

---

## 📊 Documentação da API (Swagger)

### **Acesso à Documentação**
- **URL:** `http://localhost:8081/tasklist/doc/tasklist/v1/swagger-ui/index.html`

### **Estrutura da Documentação**
A documentação está organizada em 3 tags principais:

1. **Auth** - Autenticação e gerenciamento de sessão
2. **User** - Gerenciamento de usuários do sistema
3. **Tasklist** - Gerenciamento de listas de tarefas

---

## 🔐 Sistema de Autenticação

### **Fluxo de Autenticação**
```
1. Registro → POST /api/v1/auth/register
2. Login → POST /api/v1/auth/login (recebe JWT)
3. Uso da API → Incluir header: Authorization: Bearer <token>
4. Refresh → POST /api/v1/auth/refresh-token (renova token)
5. Logout → POST /api/v1/auth/logout (invalida token)
```

### **Segurança Implementada**
- ✅ **JWT com tempo de expiração**
- ✅ **Refresh token para renovação**
- ✅ **Validação de autorização por perfil**
- ✅ **Proteção contra acesso não autorizado**
- ✅ **CORS configurado para front-end**

---

## 🗃️ Estrutura do Banco de Dados

### **Schema Principal**
```sql
-- Schema: sansys_database
-- Tabelas principais:
--   users: Armazena dados dos usuários
--   tasklists: Armazena listas de tarefas
--   (outras tabelas conforme necessidade)
```

### **Migrações Automáticas**
- O schema é atualizado automaticamente ao iniciar a aplicação
- Dados de exemplo podem ser carregados via `data.sql`

---

## 🎨 Front-end Vue.js

### **Características Implementadas**
✅ **Tela inicial com opções de login/registro**  
✅ **Dashboard após autenticação**  
✅ **CRUD completo de tarefas**  
✅ **Gerenciamento de perfil do usuário**  
✅ **Feedback visual com mensagens**  
✅ **Redirecionamento inteligente**  
✅ **Interface responsiva**  

### **Fluxo de Navegação**
```
Home → Login/Register → Dashboard → 
  ├── Minhas Tarefas (listar/criar/editar)
  ├── Meu Perfil (visualizar/editar)
  └── Logout
```

---

## 🔧 Configurações Técnicas

### **Back-end (Spring Boot)**
```yaml
# application.yml principal
server:
  port: 8081
  servlet:
    context-path: /tasklist

spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  
  # Configurações de segurança JWT
  jwt:
    secret: ${JWT_SECRET:mySecretKey}
    expiration: 86400000 # 24 horas
```

### **Dependências Atualizadas**
Durante o desenvolvimento, foram necessárias atualizações:

1. **Swagger:** 2.0.4 → 2.8.5 (correção de compatibilidade)
2. **Dependências do Spring Boot:** Versões atualizadas
3. **Vue.js e plugins:** Versões compatíveis

---

## 📱 Funcionalidades Implementadas

### **Autenticação e Usuários**
- [x] Registro de novos usuários
- [x] Login com JWT
- [x] Refresh token automático
- [x] Logout com invalidação de token
- [x] Perfis de usuário (USER/ADMIN)
- [x] Gerenciamento de perfil próprio
- [x] Listagem de usuários (apenas ADMIN)

### **Gerenciamento de Tarefas**
- [x] CRUD completo de tasklists
- [x] Listagem de tarefas do usuário
- [x] Marcar tarefas como concluídas
- [x] Validação de propriedade (usuário só acessa suas tarefas)
- [x] Filtros e buscas

### **Interface do Usuário**
- [x] Formulários com validação
- [x] Feedback visual (sucesso/erro)
- [x] Ícone para mostrar/ocultar senha
- [x] Largura padronizada de campos
- [x] Redirecionamento automático após alteração de credenciais
- [x] Design responsivo

### **Segurança**
- [x] Autenticação JWT
- [x] Autorização por perfis
- [x] Proteção de rotas
- [x] CORS configurado
- [x] Validação de entrada
- [x] Tratamento de exceções

---

## 🐛 Solução de Problemas Encontrados

### **Problema 1: Compatibilidade do Swagger**
**Situação:** Versão 2.0.4 apresentava problemas de compatibilidade com as dependências do Spring Boot.

**Solução:** Atualização para versão 2.8.5 com reconfiguração dos beans de documentação.

### **Problema 2: Configuração de Ambiente**
**Situação:** Máquina nova sem configurações prévias de Java e dependências.

**Solução:**
1. Instalação do Java 21
2. Configuração do Gradle wrapper
3. Instalação do Lombok
4. Ajuste de dependências obsoletas

### **Problema 3: Experiência com Vue.js**
**Situação:** Experiência prévia com Vue.js.

**Solução:**
1. Estudo rápido da documentação
2. Adaptação de conhecimentos em React/Angular
3. Uso de componentes Vue.js básicos
4. Foco em funcionalidade primeiro, otimização depois

---

## 🧪 Testes e Validações

### **Testes Realizados**
1. **Testes de API:**
   - Registro e login de usuários
   - CRUD de tarefas
   - Validação de autorizações
   - Testes de erros e casos limite

2. **Testes de Interface:**
   - Navegação entre telas
   - Formulários e validações
   - Responsividade
   - Estado da aplicação

3. **Testes de Integração:**
   - Comunicação front-end/back-end
   - Gerenciamento de tokens
   - Atualização em tempo real

### **Casos de Teste Críticos**
```bash
# 1. Registro com email duplicado
curl -X POST /register com email existente → 409 Conflict

# 2. Acesso não autorizado
curl -X GET /tasklists sem token → 401 Unauthorized

# 3. Acesso a tarefas de outro usuário
curl -X GET /tasklists/{id} de outro usuário → 403 Forbidden

# 4. Atualização com token expirado
curl -X PUT /user/me com token expirado → 401 → Refresh → 200
```

---

## 📈 Decisões Técnicas e Justificativas

### **1. Manutenção da Arquitetura Existente**
**Decisão:** Manter a arquitetura hexagonal do projeto original.

**Justificativa:** 
- Respeitar a base existente
- Facilitar manutenção futura
- Evitar retrabalho desnecessário
- Demonstrar capacidade de trabalhar em código legado

### **2. Branch dev/flavio e Merge para main**
**Decisão:** Criar branch de desenvolvimento e depois merge.

**Justificativa:**
- Simular ambiente de produção real
- Demonstrar fluxo Git profissional
- Manter histórico organizado
- Permitir rollback se necessário

### **3. Implementação de Perfil ADMIN**
**Decisão:** Adicionar campo `roles` no registro para permitir criação de ADMIN.

**Justificativa:**
- Demonstrar conhecimento em autorização
- Permitir futuras funcionalidades administrativas
- Mostrar flexibilidade do sistema

### **4. Redirecionamento Automático após Alteração de Credenciais**
**Decisão:** Forçar novo login após alteração de email/senha.

**Justificativa:**
- Segurança: tokens antigos devem ser invalidados
- Experiência do usuário: feedback claro sobre necessidade de novo login
- Boa prática: similares a serviços como Gmail, Facebook

---

## 📞 Suporte e Contato

### **Em Caso de Dúvidas**
- **Email:** suporte.flavio.souza@hotmail.com
- **WhatsApp:** (48) 98446-5792
- **GitHub Issues:** [Projeto no GitHub](https://github.com/flavioavds/fullstack2/issues)

### **Informações para Avaliação**
- **Tempo de Desenvolvimento:** Dentro do previsto
- **Pontos Fortes:** Arquitetura limpa, segurança robusta, documentação completa
- **Áreas de Melhoria:** Testes automatizados, performance otimizada

---

## 🎯 Conclusão

Este projeto demonstra **capacidade técnica completa** em desenvolvimento Fullstack, incluindo:

✅ **Back-end robusto** com Spring Boot e arquitetura hexagonal  
✅ **Front-end moderno** com Vue.js e experiência do usuário  
✅ **Integração perfeita** entre componentes  
✅ **Segurança implementada** com JWT e autorizações  
✅ **Documentação completa** com Swagger  
✅ **Código organizado** e pronto para produção  
✅ **Resolução de problemas** técnicos complexos  

O candidato demonstrou **adaptabilidade** ao trabalhar com tecnologias não totalmente familiares (Vue.js), **atenção aos detalhes** na configuração do ambiente, e **pensamento crítico** na solução de problemas de compatibilidade.

---

**Agradeço pela oportunidade e estou à disposição para quaisquer esclarecimentos ou discussões técnicas adicionais.**

*Documentação atualizada em Janeiro de 2025*  
*Candidato: Flavio Augusto Venancio de Souza*  
*Processo Seletivo: JTech Soluções em Informática*

# TaskList API - Documentação Técnica

## 📋 Visão Geral
API RESTful para gerenciamento de tarefas com sistema completo de autenticação e controle de sessão.

### **Acesso a Documentação SWAGGER**
- **URL Base:** `http://localhost:8081/tasklist/doc/tasklist/v1/swagger-ui/index.html#` (Documentação)

### **Informações Básicas**
- **Título:** Desafio Técnico Fullstack -JTech
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

- **Candidato:** Flavio Augusto Venancio de Souza
- **Email:** suporte.flavio.souza@hotmail.com

---

*Documentação gerada com base na especificação OpenAPI 3.1.0*