<div align="center">
    <h1> Plano de Teste </h1>
</div>

# 1. Projeto: ServeTest
- Verificar e testar os endpoints "/Login", "/Usuários", "/Produtos" e "/Carrinho" e seus respectivos verbos HTTP (GET, POST, PUT, DELETE) da API [ServeRest](https://serverest.dev/#/).
- Para rodar os testes é necessário instalar o Docker, que facilita a configuração e execução do ambiente de testes local, garantindo consistência e isolamento dos testes.

# 2. Resumo
O objetivo do teste é garantir que as áreas descritas acima sejam testadas utilizando RestAssured no Java, para que o projeto consiga seguir o fluxo de compras de maneira funcional, com o fim de atingir seu objetivo final, ou seja, cadastrar/logar/vender um produto. Para isso, foi baseado nos critérios de aceitação presentes nas User Stories (US): US|001 Usuários, US|002 Login, US|003 Produtos e US|004 Carrinho.

![testes](https://github.com/carolprotasio/ServeRest-RestAssured/blob/master/assets/total_testpng.png)
## - Swagger 
#### [O Swagger do projeto](https://serverest.dev/#)
  ![swagger](https://github.com/carolprotasio/ServeRest-RestAssured/blob/master/assets/swagger.png)

# 3. Pessoas Envolvidas
- Testadora: Carol Protásio;
- Público-alvo: Compradores e Vendedores da E-Commerce artificial ServeRest.

# 4. Funcionalidades e Módulos à Serem Testados

## 4.1 👫 **[/Usuarios]**

### **DoR e DoD**
- DoR
  - Banco de dados e infraestrutura para desenvolvimento disponibilizados;
  - Ambiente de testes disponibilizado.
- DoD

  - CRUD de cadastro de vendedores (usuários) implementado (CRIAR, ATUALIZAR, LISTAR E DELETAR);
  - Análise de testes cobrindo todos verbos;
  - Matriz de rastreabilidade atualizada;
  - Automação de testes baseado na análise realizada;

### **Critérios de Aceitação**
- Todos os usuários deverão possuir um cadastro contendo os seguintes campos: NOME, EMAIL, PASSWORD e ADMINISTRADOR;
- Não deverá ser possível fazer ações com usuários inexistentes;
- Não deve ser possível criar um usuário com e-mail já utilizado;
- Um novo usuário deverá ser criado caso não seja encontrado ID existente informado no PUT;
- Não deve ser possível cadastrar usuário com e-mail já utilizado;
- E-mails devem seguir um padrão válido de e-mail para o cadastro;

###  **Casos de Teste**
- CT-001: Cadastrar novo usuário com dados válidos com sucesso;
- CT-002: Cadastrar novo usuário com Email Inválido - sem @;
- CT-003: Cadastrar novo usuário com campo da EMAIL vazio;
- CT-004: Cadastrar novo usuário com campo da SENHA vazio;
- CT-005: Cadastrar novo usuário com campo da NOME vazio;
- CT-006: Cadastrar novo usuário com campo da ADMIN vazio;
- CT-007: Cadastrar novo usuário com TODOS campos vazio;
- CT-008: Cadastrar novo usuário com EMAIL já cadastrado;
- CT-009: Listar usuários cadastrados;
- CT-010: Listar usuário pelo ID;
- CT-011: Listar usuários pelo EMAIL;
- CT-012: Listar usuários pelo NOME;
- CT-013: Listar usuários pelo PASSWORD;
- CT-014: Listar usuários pelo ADMINISTRADOR;
- CT_015: CT-015: Validar usuário não encontrado;
- CT_016: CT-016: Alterar dados de um usuário com o ID;
- CT_017: CT-017: Alterar dados de um usuário com campo NOME vazio;
- CT-018: Alterar dados de um usuário com campo EMAIL vazio;
- CT-019: Alterar dados de um usuário com campo PASSWORD vazio;
- CT-020: Alterar dados de um usuário com campo ADMINISTRADOR vazio;
- CT-021: Excluir um usuário com ID inexistente;
- CT-022: Excluir um usuário com ID existente;
- CT-023: Excluir um usuário previamente excluído;

  ![UsuáriosCaso de Teste](https://github.com/carolprotasio/ServeRest-RestAssured/blob/master/assets/usuario_casoTest.png)


### **Testes Candidatos à Automação**
- Todos.


## 4.2 **🔑 [/Login]**


### **DoR e DoD**
- **DoR**
  - Banco de dados e infraestrutura para desenvolvimento disponibilizados;
  - API de cadastro de usuários implementada;
  - Ambiente de testes disponibilizado.

- **DoD**
  - Autenticação com geração de token Bearer implementada;
  - Análise de testes cobrindo a rota de login;
  - Matriz de rastreabilidade atualizada;
  - Automação de testes baseado na análise realizada;

###  **Critérios**
- Usuários não cadastrados não deverão conseguir autenticar;
- Usuários com senha inválida não deverão conseguir autenticar;
- No caso de não autenticação, deverá ser retornado um status code 401 (Unauthorized);
- Usuários existentes e com a senha correta deverão ser autenticados;
- A autenticação deverá gerar um token Bearer;
- A duração da validade do token deverá ser de 10 minutos;

###  **Casos de Teste**
- CT_001: Logar com email e senha corretos (já cadastrado)
- CT_002: Logar com email e senha corretos, mas com o mesmo email em CAPSLOCK;
- CT_003: Logar com email e senha inválidos (não cadastrados);
- CT_004: Login com email no formato invalido = sem @;
- CT_005: Logar com email correto, mas com senha incorreta;
- CT_006: Logar com email incorreto, mas com senha correta;
- CT_007: Logar com o campo 'senha' em branco;
- CT_008: Logar com o campo 'email' em branco;
- CT_009: Logar com o(s) campo(s) 'email' e 'senha' em branco;
- CT_010: Após sucesso ao autenticar, verificar se foi gerado o Token de acesso;

![Login Caso de Teste](https://github.com/carolprotasio/ServeRest-RestAssured/blob/master/assets/login_casoTest.png)

###  **Testes Candidatos à Automação**
- Todos.

## 4.3 **📦 [/Produtos]**

### **DoR e DoD**
- **DoR**
  - Banco de dados e infraestrutura para desenvolvimento disponibilizados;
  - API de cadastro de usuários implementada;
  - API de autenticação implementada;
  - Ambiente de testes disponibilizado.

- **DoD**
  - CRUD de cadastro de Produtos implementado (CRIAR, ATUALIZAR, LISTAR E DELETAR);
  - Análise de testes cobrindo a rota de produtos;
  - Matriz de rastreabilidade atualizada;
  - Automação de testes baseado na análise realizada;

### **Critérios**
- Usuários não autenticados não devem conseguir realizar ações na rota Produtos;
- Não deve ser possível realizar o cadastro de produtos com nomes já utilizados;
- Caso não exista produto com o o ID informado na hora do UPDATE, um novo produto deverá ser criado;
- Produtos criados através do PUT não poderão ter nomes previamente cadastrados;
- Não deve ser possível excluir produtos que estão dentro de carrinhos (dependência API Carrinhos);

### **Casos de Teste**
- CT_001: Cadastrar produtos como usuário não autenticado;
- CT_002: Cadastrar novo produto  & extrair Produto ID;
- CT_003: Cadastrar Produto com mesmo Nome;
- CT_004: Cadastrar Produto com Campos Vazio;
- T_005: Cadastrar um produto com preço 0 ou negativo;
- CT_006: Alterar o nome de um produto;
- CT_007: Alterar o nome de um produto por outro com nome já utilizado;
- CT_008: Listar produtos cadastrados;
- CT_009: Listar produtos cadastrados por ID.
- CT_010: Rota proíbida pelo usuário no cadastro de produtos;
- CT_011: Excluir um produto com com token Bearer, ausente, inválido ou expirado;
- CT_012: Excluir um produto com ID inexistente;
- CT_013: Excluir um produto com sucesso;

  ![Produto Caso de Teste](https://github.com/carolprotasio/ServeRest-RestAssured/blob/master/assets/produto_casoTest.png)

### **Testes Candidatos à Automação**
- Todos.

## 4.4 **🛒 [/Carrinho]**

### **DoR e DoD**
- **DoR**
  - Banco de dados e infraestrutura para desenvolvimento disponibilizados;
  - API de cadastro de usuários implementada;
  - API de autenticação implementada;
  - API de menu de produtos implementada;
  - Ambiente de testes disponibilizado.

- **DoD**
  - CRUD de criação de carrinhos implementado (CRIAR, ATUALIZAR,  LISTAR E DELETAR);
  - Análise de testes cobrindo a rota de carrinho;
  - Matriz de rastreabilidade atualizada;
  - Automação de testes baseado na análise realizada;

### **Critérios**
- Usuários não cadastrados não deverão conseguir realizar ações na rota Carrinho;
- Valor total do carrinho é atualizado automaticamente ao adicionar/excluir/alterar a quantidade de produtos;
- Não é possível adicionar produto não existente no carrinho;
- Não é possível ter mais de um carrinho por usuário
- Não é possível adicionar a quantidade de um produto maior do que a do estoque;
- Não é possível excluir carrinhos com produtos;
- Sistema deve verificar se o produto ainda está em estoque, bem como sua quantidade, ao ser colocado em um carrinho;
- Itens poderão ser removidos e ter sua quantidade alterada no carrinho;
- Carrinho deve ser vinculado ao token;
- Após a conclusão de compra, o carrinho deve ser excluído;
- Após o cancelamento da compra, o estoque deve ser reabastecidos com a mesma quantidade de produtos que saiu;

### **Casos de Teste**
- CT_001 => Cadastrar um produto no carrinho sem estar autenticado;
- CT_002 => Cadastrar produto 1;
- CT_003 => Cadastrar produto 2;
- CT_004 => Cadastrar um produto com quantidades maiores do que a do estoque no carrinho;
- CT_005 => Cadastrar um produto inexistente no carrinho;
- CT_006 => Cadastrar um produto com a quantidade 0;
- CT_007 => Cadastrar Carrinho;
- CT_008 => Cadastrar um produto (produto 1) que já existe no carrinho;
- CT_009 => Listar carrinho;
- CT_010 => Listar Carrinho por ID existente;
- CT_011 => Listar carrinho por ID inexistente;
- CT_012 => Concluir uma compra sem estar autenticado;
- CT_013 => Concluir uma compra;
- CT_014 => Cancelar uma compra sem estar autenticado;
- CT_016 => Cancelar uma compra;

![carrinho Caso de Teste](https://github.com/carolprotasio/ServeRest-RestAssured/blob/master/assets/carrinho_casoTest.png)

### **Testes Candidatos à Automação**
- Todos.

# 5. Local dos Testes
Todos os testes foram criados e testados localmente em meu computador pessoal pelo endereço: http://localhost:3000, o qual emula o ambiente da API.

# 6. Tecnologias utilizadas
    - IntelliJ (Java IDE);
    - RestAssured;
    - JUnit;
    - Bibliotecas Maven;
    - Bibliotecas JavaFaker;
    - Docker;

# 7. Conclusão do Projeto
O projeto ServeTest tem como objetivo validar e garantir o correto funcionamento dos endpoints críticos da API ServeRest, abrangendo as rotas "/Login", "/Usuários", "/Produtos" e "/Carrinho". Utilizando a ferramenta RestAssured em Java, foi possível criar um conjunto abrangente de testes automatizados que asseguram que todas as funcionalidades principais da aplicação estão operando conforme os critérios de aceitação definidos.

Os testes foram realizados localmente em um ambiente controlado, utilizando o Docker para emular o ambiente da API. Esta abordagem permitiu uma configuração mais rápida e consistente do ambiente de testes, além de facilitar a integração contínua e a automação dos testes. O uso de tecnologias como IntelliJ, JUnit, Maven e JavaFaker, combinado com a flexibilidade proporcionada pelo Docker, garantiu que os testes fossem executados de forma eficiente e reproduzível.

