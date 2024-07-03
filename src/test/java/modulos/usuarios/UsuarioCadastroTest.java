package modulos.usuarios;

import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Validação da funcionalidade => Cadastro de Usuário")
public class UsuarioCadastroTest {

    private String id;

    @BeforeEach
    public void beforeEach() {
        baseURI = "http://localhost";
        port = 3000;
    }
    @Test
    @Order(1)
    @DisplayName("Teste: cadastrar novo usuário com dados válidos com sucesso")
    public void testCadastrarDadosValidos() {

         id = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerNewUser())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
             .extract()
                 .path("_id");

    }

    @Test
    @Order(2)
    @DisplayName("Teste: cadastro com Email Inválido")
    public void testCadastrarUserEmailInvalido() {
            given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerUserInvalidEmail())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(400)
                .body("email", equalTo("email deve ser um email válido"));
    }
    @Test
    @Order(3)
    @DisplayName("Teste: cadastro com campo da EMAIL vazio")
    public void testCadastrarUserCampoEmailVazio() {
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerUserEmptyEmail())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(400)
                .body("email", equalTo("email não pode ficar em branco"));
    }
    @Test
    @Order(4)
    @DisplayName("Teste: cadastro com campo da SENHA vazio")
    public void testCadastrarUserCampoPasswordVazio() {
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerUserEmptyPassword())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(400)
                .body("password", equalTo("password não pode ficar em branco"));
    }
    @Test
    @Order(5)
    @DisplayName("Teste: cadastro com campo da NOME vazio")
    public void testCadastrarUserCampoNomeVazio() {
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerUserEmptyName())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(400)
                .body("nome", equalTo("nome não pode ficar em branco"));
    }
    @Test
    @Order(6)
    @DisplayName("Teste: cadastro com campo da ADMIN vazio")
    public void testCadastrarUserCampoAdmVazio() {
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerUserEmptyAdmin())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(400)
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }
    @Test
    @Order(7)
    @DisplayName("Teste: cadastro com TODOS campos vazio")
    public void testCadastrarUserTodosCamposVazio() {
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerUserEmptyAllData())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(400)
                .body("nome", equalTo("nome não pode ficar em branco"))
                .body("email", equalTo("email não pode ficar em branco"))
                .body("password", equalTo("password não pode ficar em branco"))
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }
    @Test
    @Order(8)
    @DisplayName("Test: cadastrar usuario com EMAIL já cadastrado")
    public void testCadastrarUsuarioComEmailCadastrado() {

        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerUserEmailJaCadastrado())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"));

    }

    @AfterAll
    @DisplayName("DELETE => Massa de dados")
    public void testDeleteUsuario() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/usuarios/" + id)
                .then()
                .assertThat()
                .body("message", equalTo("Registro excluído com sucesso"))
                .statusCode(200);
    }

}
