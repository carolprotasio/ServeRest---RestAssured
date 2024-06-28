package modulos.produtos;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar os verbos HTTP do => Produtos ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoVerbsTest {

    private String token;
    private String produtoId;
    private String noAdm;

    @BeforeEach
    public void beforeEach() {
        baseURI = "http://localhost";
        port = 3000;

        token = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"carol@qa.com\",\n" +
                        "  \"password\": \"123456\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .body("message", equalTo("Login realizado com sucesso"))
                    .extract()
                        .path("authorization");
    }

    @Test
    @Order(1)
    @DisplayName("Test: POST => Cadastrar novo produto")
    public void testCadastrarNovoProduto() {
        System.out.println(token);
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
    @Order(2)
    @DisplayName("Test: GET => Listar produtos cadastrados")
    public void testListarProduto() {

         given()
                .contentType(ContentType.JSON)
            .when()
                .get("/produtos")
            .then()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(3)
    @DisplayName("Test: GET => Listar produtos cadastrados por ID")
    public void testListarProdutoComId() {

        given()
                .contentType(ContentType.JSON)
            .when()
                .get("/produtos/" + produtoId)
            .then()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(4)
    @DisplayName("Test: GET => Listar produtos cadastrados por NOME")
    public void testListarProdutoporNome() {

        given()
                .contentType(ContentType.JSON)
                .queryParam("nome", "Logitech")
            .when()
                .get("/produtos/")
            .then()
                .log().all()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(5)
    @DisplayName("Test: GET => Listar produtos cadastrados por Preço")
    public void testListarProdutoporPreco() {

        given()
                .contentType(ContentType.JSON)
                .queryParam("preco", 470)
            .when()
                .get("/produtos/")
            .then()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(6)
    @DisplayName("Test: GET => Listar produtos cadastrados por Descrição")
    public void testListarProdutoporDescricao() {

        given()
                .contentType(ContentType.JSON)
                .queryParam("descricao", "TV")
            .when()
                .get("/produtos/")
            .then()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(7)
    @DisplayName("Test: GET => Listar produtos cadastrados por Quantidade")
    public void testListarProdutoporQuantidade() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("quantidade", 53)
            .when()
                .get("/produtos/")
            .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    @Order(8)
    @DisplayName("Test: PUT => Editar produto")
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
    @Order(9)
    @DisplayName("Test: DELETE => Excluir produto")
    public void testExcluirProduto() {
        System.out.println(produtoId);
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
    @Test
    @Order(10)
    @DisplayName("Test: DELETE => Excluir produto não sendo administrador")
    public void testExcluirProdutoNaoSendoAdministrador() {

        noAdm = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"noAdm@qa.com\",\n" +
                        "  \"password\": \"123456\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .body("message", equalTo("Login realizado com sucesso"))
                .extract()
                .path("authorization");

        given()
                .contentType(ContentType.JSON)
                .header("authorization", noAdm)
            .when()
                .delete("/produtos/" + produtoId)
            .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("Rota exclusiva para administradores"));
    }


}
