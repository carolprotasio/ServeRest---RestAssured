package modulos.login;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import pojo.UsuarioPojo;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Validar funcionalidade no modulo de Login")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginCasosDeTest {

    private String userId;
    private String email;
    private String password;
    private String token;

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
    @DisplayName("CT_001: Logar com email e senha corretos (já cadastrado)")
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
    @DisplayName("CT_002: Logar com email e senha corretos, mas com o mesmo email em CAPSLOCK")
    public void testLoginEmailCapslock() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"CTEST@QA.COM\",\n" +
                        "  \"password\": \"123456\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }
    @Test
    @Order(3)
    @DisplayName("CT_003: Logar com email e senha inválidos (não cadastrados)")
    public void testLoginEmailNaoCadastrado() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"naoCadastrado@qa.com\",\n" +
                        "  \"password\": \"123456\"\n" +
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
    @DisplayName("CT_004: Login com email no formato invalido = sem @")
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
    @Order(5)
    @DisplayName("CT_005: Logar com email correto, mas com senha incorreta")
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
    @Order(6)
    @DisplayName("CT_006: Logar com email incorreto, mas com senha correta")
    public void testLoginEmailInvalido() {

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"incorretoEmail@qa.com\",\n" +
                        "  \"password\": \"" + email + "\"\n" +
                        "}")
                .when()
                .post("/login")
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }
    @Test
    @Order(7)
    @DisplayName("CT_007: Logar com o campo 'senha' em branco")
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
    @Order(8)
    @DisplayName("CT_008: Logar com o campo 'emal' em branco")
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
    @Order(9)
    @DisplayName("CT_009: Logar com o(s) campo(s) 'email' e 'senha' em branco")
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
    @Test
    @Order(10)
    @DisplayName("CT_010: Após sucesso ao autenticar, verificar se foi gerado o Token de acesso")
    public void testLoginGerarToken() {

        token = given()
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
        assertNotNull(token, "O token não deve ser nulo");
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
