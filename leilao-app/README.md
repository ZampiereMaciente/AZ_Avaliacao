# Front-End - Sistema de Leilões (leilao-app)

Este é o módulo cliente do **Sistema de Leilões**, desenvolvido em **Vue 2** e estruturado com **Vuetify** para fornecer uma interface web responsiva, interativa e fluida.

---

## 🛠️ Tecnologias Utilizadas

- **Core**: Vue.js 2
- **Roteamento**: Vue Router (navegação em modo Hash `#`)
- **Estilização & Componentes**: Vuetify 2 (Design System, grids responsivas, ícones Material Design)
- **Comunicação HTTP**: Axios para consumo de API REST
- **Gerenciamento de Estado**: Vuex (se necessário)

---

## 💡 Decisões de Desenvolvimento & UX/UI

Para garantir uma interface profissional e agradável para o usuário final, foram implementados os seguintes recursos:

- **Componentização de UI**: Divisão lógica e clara de responsabilidades. O código de formulários e caixas de diálogo está isolado em componentes dedicados (`ModalUnidade`, `FormularioEmpresa`, `FormularioLeilao`) facilitando a manutenção.
- **Máscaras de Entrada Dinâmicas**: CEP, CNPJ e Telefone possuem máscaras inteligentes que são formatadas em tempo real durante a digitação. Os caracteres especiais (como `.`, `-`, `/`, `( )`) são filtrados automaticamente antes do envio à API.
- **Validações de Campo em Tempo Real**: Utilização das regras de validação reativas do Vuetify para feedbacks instantâneos ao usuário sobre campos obrigatórios, formato de e-mail e quantidade de dígitos (CNPJ e telefone).
- **Modais Customizados do Vuetify**: Substituição das caixas de diálogo padrão do navegador (`window.confirm` e `window.alert`) por diálogos estilizados (`v-dialog`) equipados com ícones de alerta e erro apropriados para cada ação.
- **Segurança de Senha na Edição**: O formulário de empresas oculta a senha durante a edição. Ela é tratada como campo opcional; caso o usuário decida mantê-la vazia, a senha antiga é preservada no banco de dados. Conta também com botão para exibir/ocultar senha (ícone de olho).
- **Interruptor de Edição (Switch)**: Para evitar modificações acidentais, a visualização de empresas carrega em modo somente leitura, disponibilizando um switch "Habilitar Edição" para liberar os campos.

---

## 📸 Demonstração das Telas

### 🏠 Tela Principal (Home)
<img width="1433" height="713" alt="home" src="https://github.com/user-attachments/assets/ea7abe36-3b41-466c-9047-c3e31d06378f" />

### 🏢 Cadastro de Unidades
*Contém a grid de consulta rápida, busca textual instantânea e modal de inclusão/edição.*
<img width="1433" height="710" alt="unidade" src="https://github.com/user-attachments/assets/4040daf6-2326-47d0-92d4-e5774deb8a04" />
<img width="1431" height="711" alt="unidades" src="https://github.com/user-attachments/assets/e48b2ce1-a211-4b19-9e61-a55e964208da" />

### 🏢 Consulta de Empresas
*Grid de consulta exibindo CNPJ e Telefone formatados, com botões para exclusão e edição.*
<img width="1430" height="710" alt="empresas" src="https://github.com/user-attachments/assets/1416483b-7976-4a27-85a5-2b9985bc4e4b" />
<img width="1431" height="713" alt="empresa" src="https://github.com/user-attachments/assets/0f95ef34-ed42-4ee1-ad15-0b69e5eb0081" />

### ✍️ Formulário de Cadastro de Empresa
*Formulário com máscaras ativas, agrupamento por abas de dados e validações em tempo real.*
![Formulário Empresa Screen](./src/assets/print-cadastro-empresa.png)

### 🔨 Consulta de Leilões
*Grid exibindo o vendedor, data de início formatada e o valor total acumulado do leilão calculados dinamicamente.*
<img width="1434" height="709" alt="leiloes" src="https://github.com/user-attachments/assets/dfd668c5-1e1c-48c3-b97a-759344f6d1b2" />
<img width="1437" height="538" alt="leilao" src="https://github.com/user-attachments/assets/e02ad08b-4c3f-4224-a2b8-10ebf2ab7d81" />

---

## ⚙️ Como Executar o Front-End

### Prerrequisitos
- Node.js 14+ instalado
- NPM instalado

### Passo a Passo

1. **Instalar Dependências**:
   Navegue até a pasta `leilao-app` e instale os pacotes necessários:
   ```bash
   npm install
   ```

2. **Configuração da API**:
   O arquivo de configuração do Axios encontra-se em `src/api.js`. Por padrão, a API é consumida no endereço `http://localhost:8081`. Altere se necessário:
   ```javascript
   // src/api.js
   const api = axios.create({
     baseURL: 'http://localhost:8081'
   })
   ```

3. **Iniciar Servidor de Desenvolvimento**:
   Execute o comando abaixo para compilar e iniciar o projeto com hot-reload automático:
   ```bash
   npm run serve
   ```

   Após iniciado, acesse a aplicação em [http://localhost:8080](http://localhost:8080).
