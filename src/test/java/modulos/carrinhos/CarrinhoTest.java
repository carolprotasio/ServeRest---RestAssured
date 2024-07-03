package modulos.carrinhos;
import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import pojo.ProdutoPojo;
import pojo.UsuarioPojo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar Funcionalidade  => Carrinho")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarrinhoTest {

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
    @DisplayName("POST => Cadastrar produto 1")
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
    @Order(2)
    @DisplayName("POST => Cadastrar produto 2")
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
    @Order(3)
    @DisplayName("POST => Cadastrar Carrinho")
    public void testCadastrarCarrinho() {

        carrinhoId = given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body("{\n" +
                        "  \"produtos\": [\n" +
                        "    {\n" +
                        "      \"idProduto\": \"" + produtoId1 + "\",\n" +
                        "      \"quantidade\": " + produto1Qtd + "\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"idProduto\": \"" + produtoId2 + "\",\n" +
                        "      \"quantidade\": " + produto2Qtd + "\n" +
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
    @DisplayName("GET => Listar Carrinho por ID")
    public void testListarCarrinhoId() {

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
    @Order(6)
    @DisplayName("GET => Listar todos os Carrinhos")
    public void testListarTodosCarrinhos() {

        given()
                .contentType(ContentType.JSON)
                .header("authorization", token)
            .when()
                .get("/carrinhos" )
            .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    @Order(88)
    @DisplayName("Test: DELETE => Excluir Carrinho / Realizar Compra")
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
    @Order(99)
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
