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
    @DisplayName("Validar => Cadastrar novo produto  / extrair Produto ID")
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
    @DisplayName("Validar => Cadastrar Produto com mesmo Nome")
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
    @Order(3)
    @DisplayName("Validar => Cadastrar Produto com token ausente")
    public void testCadastrarProdutoTokenAusente() {

        given()
                .contentType(ContentType.JSON)
                //.header("Authorization", tokenAdm)
                .body(ProdutoDataFactory.registerNewProduct())
                .when()
                .post("/produtos")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }
    @Test
    @Order(4)
    @DisplayName("Validar => Listar produtos cadastrados")
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
    @DisplayName("Validar => Rota proíbida pelo usuário no cadastro de produtos ")
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
    @Order(999)
    @DisplayName("Validar=> Deletar/Excluir produto")
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
