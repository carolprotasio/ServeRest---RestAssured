package modulos.produtos;

import dataFactory.ProdutoDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar funcionalidades no modulo => Produtos ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoTest {

    private String tokenAdm;
    private String produtoId;
    private String tokenUsuario;

    @BeforeEach
    public void beforeEach() {
        baseURI = "http://localhost";
        port = 3000;

        tokenAdm = given()
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
    @DisplayName("POST => Cadastrar Produto e extrair ID")
    public void testCadastrarProduto() {

        produtoId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", tokenAdm)
                .body(ProdutoDataFactory.registerNewProduct())
            .when()
                .post("/produtos")
            .then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                    .extract()
                .path("_id");
    }
    @Test
    @Order(2)
    @DisplayName("POST => Cadastrar Produto com mesmo Nome")
    public void testCadastrarProdutoMesmoNome() {

         given()
                .contentType(ContentType.JSON)
                .header("Authorization", tokenAdm)
                .body(ProdutoDataFactory.registerProdutoMesmoNome())
            .when()
                .post("/produtos")
            .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Já existe produto com esse nome"));
    }

    @Test
    @DisplayName("POST => Cadastro de produto pelo usuário é proibida")
    public void testCadastrarProdutoComoUsuario() {

        tokenUsuario = given()
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
    @Order(3)
    @DisplayName("Post => Cadastrar Produto com token ausente")
    public void testCadastrarProdutoTokenAusente() {

        given()
                .contentType(ContentType.JSON)
                //.header("Authorization", tokenAdm)
                .body(ProdutoDataFactory.registerProdutoMesmoNome())
            .when()
                .post("/produtos")
            .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }
}
