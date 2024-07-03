package modulos.produtos;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import pojo.UsuarioPojo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar CRUD do modulo => Produtos ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoCRUDTest {

    private String token;
    private String userId;
    private String email;
    private String password;
    private  String produtoId;

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
    @DisplayName("C => Cadastrar novo produto")
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
    @Order(2)
    @DisplayName("R => Listar produtos cadastrados por ID")
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
    @Order(3)
    @DisplayName("U => Editar produto")
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
    @Order(4)
    @DisplayName("D => Deletar/Excluir produto")
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
