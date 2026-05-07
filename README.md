# SisIncidentes — Sistema de Gerenciamento de Incidentes

O **SisIncidentes** é uma solução robusta para o registro, controle e análise de incidentes de segurança. O projeto foi recentemente migrado de uma arquitetura baseada em Firebase para uma arquitetura moderna e escalável utilizando **Java 21** e **Spring Boot 3**.

---

## 🚀 Novas Especificações Técnicas

O sistema agora opera em uma estrutura de **Monorepo**, dividida em duas camadas principais:

### ⚙️ Backend (API)
- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.4.5
- **Persistência:** Spring Data JPA com Hibernate
- **Banco de Dados:** H2 Database (em memória para desenvolvimento)
- **Segurança:** Spring Security (Configurado com bypass para ambiente de desenvolvimento)
- **Documentação:** OpenAPI 3 / Swagger UI
- **Gerenciador de Dependências:** Maven

### 💻 Frontend (Web)
- **Framework:** React 19+
- **Estilização:** Bootstrap 5.3 & React-Bootstrap
- **Roteamento:** React Router DOM 7
- **Gráficos:** Recharts
- **Ícones:** React Icons

---

## 📂 Estrutura do Projeto

- `/api`: Código-fonte do backend Spring Boot.
- `/web`: Código-fonte do frontend React.

---

## ⚙️ Como Executar o Projeto

### 1️⃣ Preparação do Backend
1. Navegue até a pasta `api`:
   ```bash
   cd api
   ```
2. Execute o projeto usando Maven:
   ```bash
   mvn spring-boot:run
   ```
   *O servidor iniciará em `http://localhost:8080`.*

### 2️⃣ Preparação do Frontend
1. Navegue até a pasta `web`:
   ```bash
   cd web
   ```
2. Instale as dependências:
   ```bash
   npm install
   ```
3. Verifique o arquivo `.env` para garantir a URL da API:
   ```env
   REACT_APP_API_URL=http://localhost:8080/api
   ```
4. Inicie o servidor de desenvolvimento:
   ```bash
   npm start
   ```
   *A aplicação abrirá em `http://localhost:3000`.*

---

## 🧪 Passo a Passo para Testar o Projeto

### A. Testando o Backend (API)

1.  **Swagger UI (Exploração de Endpoints):**
    - Acesse `http://localhost:8080/swagger-ui.html` para visualizar e testar todos os endpoints disponíveis.
    - No módulo **Unidades Administrativas (RF01)**, você pode realizar testes de `GET`, `POST`, `PUT` e `DELETE` diretamente pelo navegador.

2.  **Console do Banco de Dados H2:**
    - Acesse `http://localhost:8080/h2-console`.
    - **JDBC URL:** `jdbc:h2:mem:sisincidentesdb`
    - **Usuário:** `sa`
    - **Senha:** (deixe em branco)
    - Clique em "Connect" para visualizar as tabelas e os dados persistidos em tempo de execução.

3.  **Testes Automatizados:**
    - Na pasta `api`, execute:
      ```bash
      mvn test
      ```
    - Isso validará as regras de negócio e a integração dos controllers.

### B. Testando o Frontend (Integração)

1.  **Fluxo de Unidade Administrativa (RF01):**
    - No sistema, navegue até a seção de "Unidades".
    - **Cadastro:** Tente cadastrar uma nova unidade (Ex: SIGLA: STI, Título: Superintendência de TI).
    - **Listagem:** Verifique se a unidade aparece na lista após o cadastro.
    - **Busca:** Utilize o campo de pesquisa para filtrar por sigla ou título.
    - **Edição:** Altere o título de uma unidade e verifique se a alteração persiste.
    - **Exclusão:** Remova uma unidade e confirme se ela sumiu da listagem e do console H2.

2.  **Validações:**
    - Tente cadastrar uma unidade com o mesmo **código** de uma já existente; o sistema deve retornar um erro amigável de conflito (409).
    - Tente salvar campos vazios para verificar a validação de formulários.

---

## 🛠️ Requisitos de Ambiente
- Java 21 JDK
- Node.js 18+
- Maven 3.8+
