# Back-End - API REST do Sistema de Leilões (leilao-api)

Este é o módulo servidor do **Sistema de Leilões**, projetado como uma API REST robusta desenvolvida em **Spring Boot** com persistência em banco de dados **PostgreSQL** e cobertura de testes automatizados.

---

## 🛠️ Tecnologias Utilizadas

- **Framework**: Spring Boot 2.x / Java 11+
- **Acesso a Dados**: Spring Data JPA / Hibernate (ORM)
- **Banco de Dados**: PostgreSQL 10.5+
- **Validação**: Hibernate Validator (Bean Validation `@Valid`)
- **Segurança & Criptografia**: Spring Security (BCrypt)
- **Testes**: JUnit 5, Mockito, Spring Boot Test, MockMvc
- **Containerização**: Docker / Docker Compose

---

## 💡 Decisões de Desenvolvimento & Arquitetura

O backend foi desenvolvido seguindo boas práticas de projeto e padrões modernos de APIs RESTful:

- **Arquitetura em Camadas**:
  - `service`: Controladores REST mapeando as rotas da API, métodos HTTP (GET, POST, PUT, DELETE) e cabeçalhos CORS.
  - `business` (BO): Camada exclusiva para regras de negócio, validações de consistência e operações transacionais.
  - `repository`: Interfaces de persistência estendendo `JpaRepository` com consultas otimizadas.
  - `entity`: Mapeamento objeto-relacional das tabelas físicas do banco de dados PostgreSQL.
- **Uso de DTOs (Data Transfer Objects)**: Separação completa entre as entidades de banco de dados e os dados de tráfego de entrada (`RequestDTO`) e de resposta (`ResponseDTO`). Isso protege as colunas internas e previne problemas como loops de serialização.
- **Tratamento de Exceções Centralizado (`ApiExceptionHandler`)**:
  - Implementação com `@RestControllerAdvice` para interceptar erros globalmente.
  - Retorno padronizado de payloads de erro estruturados (`ErrorResponse` e `ValidationErrorResponse`), contendo carimbo de data/hora, status HTTP, título e detalhamento legível por humanos.
  - Mapeamento customizado de violações de chave única/duplicidade (`DuplicateResourceException`) e violações de integridade referencial ao excluir registros com dependências ativas (`DeleteConflictException`).
- **Segurança de Acesso**: Criptografia de senhas usando o algoritmo de hash BCrypt no salvamento de empresas.
- **Configuração do CORS**: Habilitação explícita do CORS (`@CrossOrigin`) para aceitar requisições de origem do cliente local `http://localhost:8080`.

---

## 🧪 Testes Automatizados (JUnit 5)

Foi implementada uma suite de testes unitários e de integração utilizando JUnit 5 e Mockito para garantir o funcionamento correto e a estabilidade das principais operações do sistema:

### 1. Testes de Integração de Negócios (`EmpresaBOTest`)
Focados em validar o fluxo de ponta a ponta na camada de lógica (`EmpresaBO`) persistindo na base de testes com rollback automático (`@Transactional`):
- Cadastro de empresa com sucesso.
- Criptografia da senha ao salvar empresa no banco de dados.
- Lançamento de exceção `DuplicateResourceException` ao tentar usar CNPJ já cadastrado.
- Busca por ID com sucesso e tratamento amigável de erro `ResourceNotFoundException` para IDs inexistentes.

### 2. Testes de API REST / Web (`EmpresaServiceTest`)
Focados em validar o comportamento dos controladores REST usando `MockMvc` para simular requisições HTTP e validar o protocolo de respostas sem inicializar o servidor completo (`@WebMvcTest`):
- Retorno HTTP `201 Created` e payload JSON válido no POST.
- Retorno HTTP `200 OK` e listagem de empresas no GET.
- Retorno HTTP `200 OK` no GET por ID existente e `404 Not Found` para IDs inexistentes.
- Retorno HTTP `200 OK` no PUT para atualizações e no DELETE para exclusões bem-sucedidas.

### 📸 Execução dos Testes JUnit
<img width="1432" height="822" alt="testeJUnit" src="https://github.com/user-attachments/assets/5809d977-ce87-48aa-80cf-597e31630d47" />

---

## 🔌 Validação Manual & Ferramentas de Apoio

Além dos testes unitários/integração automatizados, a API foi exaustivamente inspecionada e validada manualmente através das seguintes ferramentas:

### 1. Testes de Endpoints via **Postman**
Utilizado para validar as requisições HTTP enviadas à API REST, assegurando o retorno de status adequados (ex: `201 Created` ao criar registros) e mensagens de erro estruturadas.
- **Cenário de Teste**: Criação/Inclusão de nova Empresa via POST `/empresas` com corpo JSON completo e validações.
- **Print de Validação (Postman)**:
<img width="1431" height="821" alt="postamn" src="https://github.com/user-attachments/assets/a9532e20-0632-49bb-9ae3-8ddb00923adb" />

### 2. Auditoria de Banco de Dados via **DBeaver**
Utilizado para conectar diretamente ao banco PostgreSQL (porta `5435`) e garantir a persistência correta, verificação de sequences, triggers e as alterações de tabelas em tempo real.
- **Cenário de Auditoria**: Verificação da inserção física das empresas na tabela, confirmando se os dados estão armazenados corretamente (como senhas criptografadas em hash BCrypt).
- **Print de Validação (DBeaver)**:
<img width="1435" height="824" alt="DBevaer" src="https://github.com/user-attachments/assets/d832f931-87c8-49b2-81b2-41a62efcea4c" />

---

## ⚙️ Como Executar o Back-End

### Prerrequisitos
- Docker e Docker Compose instalados
- Java JDK 11 ou superior instalado
- Maven configurado (ou usando o wrapper `./mvnw`)

---

### 1. Inicializar o Banco de Dados (PostgreSQL)

O projeto está configurado para executar o PostgreSQL em container Docker.
Navegue até a raiz do projeto (onde está o arquivo `docker-database.yml`) e execute:

```bash
docker-compose -f docker-database.yml up -d
```

> [!TIP]
> Os scripts SQL (`ddl.sql` e `dml.sql`) localizados na pasta `./database` estão montados como volumes e serão importados e executados de forma totalmente automatizada no primeiro arranque do banco de dados, configurando e populando as tabelas necessárias.

O banco estará ouvindo externamente na porta **`5435`**.

---

### 2. Executar a Suite de Testes

Para rodar os testes JUnit implementados e verificar a corretude da aplicação, execute na raiz da pasta `leilao-api`:

```bash
# No Linux/Mac:
./mvnw test

# No Windows:
mvnw.cmd test
```

---

### 3. Executar a Aplicação Spring Boot

Para iniciar a API REST e deixá-la disponível para o frontend, execute:

```bash
# No Linux/Mac:
./mvnw spring-boot:run

# No Windows:
mvnw.cmd spring-boot:run
```

A API REST iniciará por padrão na porta **`8081`**.
Você pode testar se a API está online acessando os endpoints expostos, por exemplo: [http://localhost:8081/unidades](http://localhost:8081/unidades).
