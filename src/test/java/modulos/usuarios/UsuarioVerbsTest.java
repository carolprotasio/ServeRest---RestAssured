package modulos.usuarios;

import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar os verbos HTTP do => Usuário ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioVerbsTest {
    private String id;

    @BeforeEach
    public void beforeEach(){
        baseURI = "http://localhost";
        port = 3000;

    }

    @Test
    @Order(1)
    @DisplayName("Test: POST => Cadastrar novo usuário e obter num do ID com sucesso")
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
        System.out.println("Cadastro novo usuario e id: " + id);

    }
    @Test
    @Order(2)
    @DisplayName("Test: POST => Validar cadastrar usuario com EMAIL já cadastrado")
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

        System.out.println("Cadastro novo usuario e id: " + id);

    }

    @Test
    @Order(3)
    @DisplayName("Test: GET => Listar usuários cadastrados")
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
    @Order(4)
    @DisplayName("Test: GET => Buscar usuário por ID")
    public void testBuscarUsuarioPorId() {

             given()
                .contentType(ContentType.JSON)
            .when()
                .get("/usuarios/" + id)
            .then()
                .assertThat()
                     .body("_id", equalTo(id))
                     .statusCode(200);

    }

    @Test
    @Order(5)
    @DisplayName("Test: PUT => Editar Usuário")
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
    @Order(6)
    @DisplayName("Test: DELETE => Excluir Usuário")
    public void testExcluirUsuario() {
        System.out.println("Cadastro novo usuario e id: " + id);

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
