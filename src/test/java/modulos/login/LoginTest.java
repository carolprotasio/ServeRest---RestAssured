package modulos.login;


import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar modulo de Login")
public class LoginTest {

    private String newUser;

    @BeforeEach
    public void beforeEach(){
        baseURI = "http://localhost";
        port = 3000;
    }

    @Test
    @DisplayName("Validar - Login com sucesso")
    public void testLoginSucesso() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"carol@qa.com\",\n" +
                        "  \"password\": \"123456\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Login realizado com sucesso"));
    }
    @Test
    @DisplayName("Validar - Login com email no formato invalido => sem @")
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
    @DisplayName("Validar - Login com senha inválida")
    public void testLoginSenhaInvalida() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"carol@qa.com\",\n" +
                        "  \"password\": \"invalido\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }
    @Test
    @DisplayName("Validar - Login com campo vazio na senha")
    public void testLoginESenhaCampoVazio() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"carol@qa.com\",\n" +
                        "  \"password\": \"\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(400)
                .body("password", equalTo("password não pode ficar em branco"));
    }
    @Test
    @DisplayName("Validar - Login com campo vazio no email")
    public void testLoginCampoVazioEmail() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \" \",\n" +
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
    @DisplayName("Validar - Login com todos os campo vazio")
    public void testLoginTodosCamposVazio() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \" \",\n" +
                        "  \"password\": \"\"\n" +
                        "}")
            .when()
                .post("/login")
            .then()
                .assertThat()
                .statusCode(400)
                .body("email", equalTo("email deve ser um email válido"))
                .body("password", equalTo("password não pode ficar em branco"));

    }



}
