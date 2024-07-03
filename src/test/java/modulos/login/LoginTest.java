package modulos.login;


import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import pojo.UsuarioPojo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Validar funcionalidade no modulo de Login")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest {

    private String userId;
    private String email;
    private String password;

    @BeforeAll
    public void beforeAll(){
        baseURI = "http://localhost";
        port = 3000;

        UsuarioPojo newUser = UsuarioDataFactory.registerNewUser();
        this.email = newUser.getEmail();
        this.password = newUser.getPassword();


        userId = given()
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
    }
    @Test
    @Order(1)
    @DisplayName("Realizar Login com sucesso")
    public void testLoginSucesso() {

         given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Login realizado com sucesso"));
    }
    @Test
    @Order(2)
    @DisplayName("Validar => Login com email no formato invalido = sem @")
    public void testLoginEmailFormatoInvalido() {
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"invalidEmail.com\",\n" +
                        "  \"password\": \"teste\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(400)
                .body("email", equalTo("email deve ser um email válido"));
    }

    @Test
    @Order(3)
    @DisplayName("Validar => Login com senha inválida")
    public void testLoginSenhaInvalida() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"" + email + "\",\n" +
                        "  \"password\": \"123\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }
    @Test
    @Order(4)
    @DisplayName("Validar => Login com campo vazio da SENHA")
    public void testLoginESenhaCampoVazio() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"email\": \"" + email + "\",\n" +
                        " \"password\": \"\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(400)
                .body("password", equalTo("password não pode ficar em branco"));
    }
    @Test
    @Order(5)
    @DisplayName("Validar => Login com campo vazio do EMAIL")
    public void testLoginCampoVazioEmail() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"email\": \" \",\n" +
                        " \"password\": \"" + password + "\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(400)
                .body("email", equalTo("email deve ser um email válido"));
    }
    @Test
    @Order(6)
    @DisplayName("Validar => Login com TODOS os campo vazio")
    public void testLoginTodosCamposVazio() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"email\": \" \",\n" +
                        " \"password\": \"\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(400)
                .body("email", equalTo("email deve ser um email válido"))
                .body("password", equalTo("password não pode ficar em branco"));

    }

    @AfterAll
    @DisplayName("DELETE => Massa de dados / Usuário")
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
