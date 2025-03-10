# Teste Técnico IDS - Desenvolvedor Fullstack Java/Angular

## 📌 Descrição do Projeto
Este projeto consiste no desenvolvimento de uma aplicação para entrada de notas fiscais, utilizando **Angular + PrimeNG** no front-end e **Java Nativo + Quarkus** no back-end, com **PostgreSQL** como banco de dados. A API deve ser documentada com **Swagger**.

## 🚀 Tecnologias Utilizadas
- **Front-End:** Angular + PrimeNG
- **Back-End:** Java Nativo + Quarkus
- **Banco de Dados:** PostgreSQL
- **Documentação da API:** Swagger (Obrigatório)
- **Diferencial:** Testes unitários no backend (não obrigatório)

> ⚠️ **Observação:** Não utilizar frameworks Java como Spring ou qualquer outro.

## 📝 Funcionalidades

### 📦 Cadastro de Produto
- Código
- Descrição
- Situação (Ativo, Inativo)
- Permitir pesquisar os produtos
- Permitir as operações de Inclusão, Edição e Exclusão
- ❌ **Restrição:** Não permitir excluir um produto que já teve movimentação

### 🏢 Cadastro de Fornecedor
- Código
- Razão Social
- E-Mail
- Telefone
- CNPJ
- Situação (Ativo, Baixado, Suspenso)
- Data da Baixa
- Permitir pesquisar os fornecedores
- Permitir as operações de Inclusão, Edição e Exclusão
- ❌ **Restrições:**
  - Não permitir mais de um fornecedor com o mesmo CNPJ
  - Não permitir excluir um fornecedor que já teve movimentação

### 📜 Entrada de Nota Fiscal
#### Informações da Nota:
- Número da nota
- Data e Horário de Emissão
- Fornecedor
- Endereço
- Valor Total da Nota

#### Itens da Nota Fiscal:
- Produto
- Valor Unitário
- Quantidade
- Exibição do valor total do item (quantidade x valor unitário)

#### Operações permitidas:
- Pesquisar notas fiscais
- Incluir, Editar e Excluir Notas
- Incluir, Editar e Excluir Itens da Nota

## 📂 Estrutura do Projeto
```
📦 projeto
├── 📂 frontend (Angular + PrimeNG)
├── 📂 backend (Java + Quarkus)
└── README.md
```

## 🔧 Como Rodar a Aplicação
### 🚀 Pré-requisitos
- [Node.js](https://nodejs.org/) e [Angular CLI](https://angular.io/cli) para o Front-End
- [JDK 17+](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) e [Quarkus](https://quarkus.io/) para o Back-End
- [PostgreSQL](https://www.postgresql.org/download/) instalado

### ⚙️ Configuração do Banco de Dados
1. Criar um banco de dados no PostgreSQL
2. Configurar as credenciais no back-end (`application.properties` ou `application.yaml`)

### ▶️ Executando o Back-End
```sh
cd backend
./mvnw quarkus:dev
```

### ▶️ Executando o Front-End
```sh
cd frontend
npm install
ng serve
```

## 🛠️ Documentação da API
Após iniciar o back-end, acesse a documentação do Swagger:
```
http://localhost:7171/q/swagger-ui/
```

## 📜 Entrega do Teste
1. Disponibilizar o código em um repositório público (GitHub, GitLab, Bitbucket, etc.)
2. Enviar o link para avaliação

---

Feito com ❤️ para o teste técnico! 😊