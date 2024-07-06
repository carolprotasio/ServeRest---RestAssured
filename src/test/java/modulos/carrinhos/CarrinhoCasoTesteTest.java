package modulos.carrinhos;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import pojo.ProdutoPojo;
import pojo.UsuarioPojo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar Funcionalidade Conforme Caso de Teste")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarrinhoCasoTesteTest {

    private String carrinhoId;
    private String token;
    private String userId;
    private String email;
    private String password;
    private String produtoId1;
    private String produtoId2;
    private int produto1Qtd;
    private int produto2Qtd;

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
    @DisplayName("CT_001 => Cadastrar um produto no carrinho sem estar autenticado")
    public void testCadastrarProdutoCarrinhoSemAutenticacao() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"produtos\": [ { \"idProduto\": \"" + produtoId1 + "\", \"quantidade\": " + produto1Qtd + " } ] }")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }

    @Test
    @Order(2)
    @DisplayName("CT_002 => Cadastrar produto 1")
    public void testCadastrarNProduto1() {
        ProdutoPojo newProduct1 = ProdutoDataFactory.registerNewProduct();
        this.produto1Qtd = newProduct1.getQuantidade();

        produtoId1 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(newProduct1)
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
    @DisplayName("CT_003 => Cadastrar produto 2")
    public void testCadastrarProduto2() {
        ProdutoPojo newProduct2 = ProdutoDataFactory.registerNewProduct();
        this.produto2Qtd = newProduct2.getQuantidade();

        produtoId2 = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(newProduct2)
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
    @Order(4)
    @DisplayName("CT_004 => Cadastrar um produto com quantidades maiores do que a do estoque no carrinho")
    public void testCadastrarProdutoQuantidadeMaiorEstoque() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{ \"produtos\": [ { \"idProduto\": \"" + produtoId1 + "\", \"quantidade\": " + (produto1Qtd + 1) + " } ] }")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Produto não possui quantidade suficiente"));
    }
    @Test
    @Order(5)
    @DisplayName("CT_005 => Cadastrar um produto inexistente no carrinho")
    public void testCadastrarProdutoInexistenteCarrinho() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{ \"produtos\": [ { \"idProduto\": \"idInexistente\", \"quantidade\": 1 } ] }")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Produto não encontrado"));
    }

    @Test
    @Order(6)
    @DisplayName("CT_006 => Cadastrar um produto com a quantidade 0")
    public void testCadastrarProdutoQuantidadeZero() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{ \"produtos\": [ { \"idProduto\": \"" + produtoId1 + "\", \"quantidade\": 0 } ] }")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .statusCode(400);

    }
    @Test
    @Order(7)
    @DisplayName("CT_007 => Cadastrar Carrinho")
    public void testCadastrarCarrinho() {
        carrinhoId = given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{ \"produtos\": [ { \"idProduto\": \"" + produtoId1 + "\", \"quantidade\": " + produto1Qtd + " }, { \"idProduto\": \"" + produtoId2 + "\", \"quantidade\": " + produto2Qtd + " } ] }")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("_id");
    }
    @Test
    @Order(8)
    @DisplayName("CT_008 => Cadastrar um produto (produto 1) que já existe no carrinho")
    public void testCadastrarProdutoJaExisteNoCarrinho() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{ \"produtos\": [ { \"idProduto\": \"" + produtoId1 + "\", \"quantidade\": " + produto1Qtd + " } ] }")
                .when()
                .post("/carrinhos")
                .then()
                .assertThat()
                .statusCode(400);

    }
    @Test
    @Order(9)
    @DisplayName("CT_009 => Listar carrinho")
    public void testListarCarrinhos() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .when()
                .get("/carrinhos")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(10)
    @DisplayName("CT_010 => Listar Carrinho por ID existente")
    public void testListarCarrinhoId() {

        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .pathParams("_id", carrinhoId)
                .when()
                .get("/carrinhos/{_id}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(11)
    @DisplayName("CT_011 => Listar carrinho por ID inexistente")
    public void testListarCarrinhoIdInexistente() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .pathParams("_id", "idInexistente")
                .when()
                .get("/carrinhos/{_id}")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Carrinho não encontrado"));
    }

    @Test
    @Order(12)
    @DisplayName("CT_012 => Concluir uma compra sem estar autenticado")
    public void testConcluirCompraSemAutenticacao() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/carrinhos/concluir-compra")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }

    @Test
    @Order(13)
    @DisplayName("CT_013 => Concluir uma compra")
    public void testConcluirCompra() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .when()
                .delete("/carrinhos/concluir-compra")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
    }
    @Test
    @Order(14)
    @DisplayName("CT_014 => Cancelar uma compra sem estar autenticado")
    public void testCancelarCompraSemAutenticacao() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/carrinhos/cancelar-compra")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }

    @Test
    @Order(15)
    @DisplayName("CT_016 => Cancelar uma compra")
    public void testCancelarCompra() {
        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .when()
                .delete("/carrinhos/cancelar-compra")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Não foi encontrado carrinho para esse usuário"));
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
