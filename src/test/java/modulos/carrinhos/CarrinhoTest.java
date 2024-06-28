package modulos.carrinhos;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar Funcionalidade CRUD => Carrinho")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarrinhoTest {

    private String carrinhoId;
    private String token;
    private String produtoId;
    private int precoTotal;
    private String idUsuario;
    private String _id;

    @BeforeEach
    public void beforeEach() {

        baseURI = "http://localhost";
        port = 3000;

        token = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"noAdm@qa.com\",\n" +
                        "  \"password\": \"123456\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(200)
                    .extract()
                        .path("authorization");
    }
    @Test
    @Order(1)
    @DisplayName("Test: GET => Listar Carrinhos Cadastrados TODOS")
    public void testListarCarrinhos() {

        produtoId = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/carrinhos")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("carrinhos[0].produtos[1].idProduto");
    }
    @Test
    @Order(2)
    @DisplayName("Test: GET => Listar Carrinhos Cadastrados por Carrinho ID")
    public void testListarCarrinhosPorId() {

        _id = given()
                .contentType(ContentType.JSON)
            .when()
                .get("/carrinhos")
            .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("carrinhos[0].-id");

        precoTotal = given()
                .contentType(ContentType.JSON)
                .param("_id", _id)
            .when()
                .get("/carrinhos")
            .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("carrinhos[0].precoTotal");

    }
    @Test
    @Order(3)
    @DisplayName("Test: GET => Listar Carrinhos Cadastrados por Preço Total")
    public void testListarCarrinhosPorIPrecoTotal() {

        given()
                .contentType(ContentType.JSON)
                .param("precoTotal", precoTotal)
                .when()
                .get("/carrinhos")
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    @Order(4)
    @DisplayName("Test: GET => Listar Carrinhos Cadastrados por Quantidade Total")
    public void testListarCarrinhosPorQuantidadeTotal() {

        idUsuario = given()
                .contentType(ContentType.JSON)
                .param("quantidadeTotal", 50)
            .when()
                .get("/carrinhos")
            .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("carrinhos[0].produtos[0].idUsuario");

    }
    @Test
    @Order(5)
    @DisplayName("Test: POST => Cadastrar Carrinhos")
    public void testCadastrarCarrinho() {

        carrinhoId = given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{\n" +
                        "  \"produtos\": [\n" +
                        "    {\n" +
                        "      \"idProduto\": \""+ produtoId +"\",\n" +
                        "      \"quantidade\": 2\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
            .when()
                .post("/carrinhos")
            .then()
                .assertThat()
                .statusCode(201)
                    .extract()
                .path("_id");
    }
    @Test
    @Order(6)
    @DisplayName("Test: GET => Buscar Carrinho por ID")
    public void testBuscarCarrinhoId() {

        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .pathParams("_id", carrinhoId)
            .when()
                .get("/carrinhos/{_id}" )
            .then()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(7)
    @DisplayName("Test: DELETE => Excluir Carrinho")
    public void testExcluirCarrinho() {

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
    @Order(8)
    @DisplayName("Test: DELETE => Excluir Carrinho quando NÂO tem carrinho para o usuário")
    public void testExcluirCarrinhoRetornarProdutosEstoque() {

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




}
