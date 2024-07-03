package modulos.usuarios;

import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar CRUD API Usuário ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioCrudTest {
    private static String id;

    @BeforeEach
    public void beforeEach(){
        baseURI = "http://localhost";
        port = 3000;
    }

    @Test
    @Order(1)
    @DisplayName("C => Cadastrar novo usuário e obter ID com sucesso")
    public void testCadastrarNovoUsuario() {

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
    @DisplayName("R => Listar usuários cadastrados")
    public void testListarUsuariosCadastrados() {

            given()
                .contentType(ContentType.JSON)
            .when()
                .get("/usuarios")
            .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    @Order(3)
    @DisplayName("U => Editar Usuário")
    public void testEditarUsuario() {

        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.userEdit())
            .when()
                .put("/usuarios/" + id)
            .then()
                .assertThat()
                .body("message", equalTo("Registro alterado com sucesso"))
                .statusCode(200);

    }
    @Test
    @Order(4)
    @DisplayName("D => Deletar Usuário")
    public void testExcluirUsuario() {

        given()
                .contentType(ContentType.JSON)
            .when()
                .delete("/usuarios/" + id)
            .then()
                .assertThat()
                .body("message", equalTo("Registro excluído com sucesso"))
                .statusCode(200);
    }
    @Test
    @Order(5)
    @DisplayName("Test => Validar usuário por ID excluido")
    public void testBuscarUsuarioPorId() {

             given()
                .contentType(ContentType.JSON)
            .when()
                .get("/usuarios/" + id)
            .then()
                .assertThat()
                     .body("message", equalTo("Usuário não encontrado"))
                     .statusCode(400);

    }




}
