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

}
