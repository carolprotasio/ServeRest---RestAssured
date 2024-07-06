package modulos.produtos;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import pojo.UsuarioPojo;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar funcionalidades no modulo => Produtos ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoTest {


    private String produtoId;
    private String token;
    private String userId;
    private String email;
    private String password;
    private String tokenUsuario;

    @BeforeAll
    public void beforeAll(){
        baseURI = "http://localhost";
        port = 3000;

        UsuarioPojo newUser = UsuarioDataFactory.registerNewUser();
        this.email = newUser.getEmail();
        this.password = newUser.getPassword();


        this.userId = given()
                .contentType(ContentType.JSON)
                .body(newUser)
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract()
                .path("_id");

        this.token =  given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Login realizado com sucesso"))
                .extract()
                .path("authorization");

    }
    @Test
    @Order(1)
    @DisplayName("CT_001: Cadastrar produtos como usuário não autenticado")
    public void testAcessarProdutoUsuarioNaoCadastrado() {

        given()
                .contentType(ContentType.JSON)
                .body(ProdutoDataFactory.registerNewProduct())
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .statusCode(401);
    }
    @Test
    @Order(2)
    @DisplayName("CT_002: Cadastrar novo produto  & extrair Produto ID")
    public void testCadastrarNovoProduto() {

        produtoId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(ProdutoDataFactory.registerNewProduct())
            .when()
                .post("/produtos")
            .then()
                .assertThat()
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .statusCode(201)
                .extract()
                .path("_id");
    }
    @Test
    @Order(3)
    @DisplayName("CT_003: Cadastrar Produto com mesmo Nome")
    public void testCadastrarProdutoMesmoNome() {

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(ProdutoDataFactory.registerProdutoMesmoNome())
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Já existe produto com esse nome"));
    }
    @Test
    @Order(4)
    @DisplayName("CT_004: Cadastrar Produto com Campos Vazio")
    public void testCadastrarProdutoCampoVazio() {

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(ProdutoDataFactory.registerProdutoCamposVazio())
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .statusCode(400)
                .body("nome", equalTo("nome não pode ficar em branco"));

    }
    @Test
    @Order(5)
    @DisplayName("CT_005: Cadastrar um produto com preço 0 ou negativo")
    public void testCadastrarProdutoCValorNegativo() {

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(ProdutoDataFactory.registerNewProductValorNegativo())
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .statusCode(400)
                .body("preco", equalTo("preco deve ser um número positivo"));

    }
    @Test
    @Order(6)
    @DisplayName("CT_006: Alterar o nome de um produto")
    public void testEditarProduto() {

        given()
                .contentType(ContentType.JSON)
                .body(ProdutoDataFactory.productEdit())
                .when()
                .get("/produtos/" + produtoId)
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    @Order(7)
    @DisplayName("CT_007: Alterar o nome de um produto por outro com nome já utilizado")
    public void testEditarProdutoNomeJaUtilizado() {

        given()
                .contentType(ContentType.JSON)
                .body(ProdutoDataFactory.registerProdutoMesmoNome())
                .when()
                .get("/produtos/" + produtoId)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(8)
    @DisplayName("CT_008: Listar produtos cadastrados")
    public void testListarProdutosCadastrados() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/produtos")
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    @Order(9)
    @DisplayName("CT_009: Listar produtos cadastrados por ID")
    public void testListarProdutosCadastradosPorId() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/produtos")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(10)
    @DisplayName("CT_010: Rota proíbida pelo usuário no cadastro de produtos ")
    public void testCadastrarProdutoComoUsuario() {

        tokenUsuario = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"email\": \"noAdm@qa.com\",\n" +
                        " \"password\": \"123456\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("authorization");
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", tokenUsuario)
                .body(ProdutoDataFactory.registerNewProduct())
                .when()
                .post( "/produtos")
                .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("Rota exclusiva para administradores"));
    }
    @Test
    @Order(11)
    @DisplayName("CT_011: Excluir um produto com com token Bearer, ausente, inválido ou expirado")
    public void testExcluirProdutoSemToken() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/produtos/" + produtoId)
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }
    @Test
    @Order(12)
    @DisplayName("CT_012: Excluir um produto com ID inexistente")
    public void testExcluirProdutoSemTokeIdInexistente() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .when()
                .delete("/produtos/" + "123456")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"));
    }


    @Test
    @Order(13)
    @DisplayName("CT_013: Excluir um produto com sucesso")
    public void testExcluirProduto() {

        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .when()
                .delete("/produtos/" + produtoId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
    }
    @AfterAll
    @DisplayName("DELETE => Massa de dados")
    public void testDeleteUsuario() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("message", equalTo("Registro excluído com sucesso"))
                .statusCode(200);
    }



}
