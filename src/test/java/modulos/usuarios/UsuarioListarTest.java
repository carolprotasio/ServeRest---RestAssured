package modulos.usuarios;

import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validar a busca por usuário por filtros ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioListarTest {
    private String admId;
    private String userId;
    private String nome;
    private String email;
    private String password;
    private String administrador;

    @BeforeEach
    public void berforeEach() {
        baseURI = "http://localhost";
        port = 3000;
    }
    @Test
    @Order(1)
    @DisplayName("Post - Cadastrar Administrador")
    public void testCadastrarAdm(){
        admId = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerNewUser())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("_id");
    }
    @Test
    @Order(2)
    @DisplayName("Post - Cadastrar Usuário")
    public void testCadastrarUsuario(){
        userId = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.registerNoAdm())
            .when()
                .post("/usuarios")
            .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("_id");
    }

    @Test
    @Order(3)
    @DisplayName("GET - Listar usuários cadastrados")
    public void testListarUsuariosCadastrados(){
        nome = given()
                .contentType(ContentType.JSON)
            .when()
                .get("/usuarios")
            .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("nome");
    }
    @Test
    @Order(4)
    @DisplayName("GET - Listar usuário pelo ID")
    public void testListarUsuariospeloID(){
        email = given()
                .contentType(ContentType.JSON)
                .param("_id", userId)
            .when()
                .get("/usuarios")
            .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("usuarios[0].email");

    }
    @Test
    @Order(5)
    @DisplayName("GET - Listar usuários pelo EMAIL")
    public void testListarUsuariospeloEmail(){
        administrador =  given()
                .contentType(ContentType.JSON)
                .param("email", email)
                .when()
                .get("/usuarios")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("usuarios[0].administrador");
        System.out.println(administrador);

    }
    @Test
    @Order(6)
    @DisplayName("GET - Listar usuários pelo NOME")
    public void testListarUsuariospeloNome(){
        password = given()
                .contentType(ContentType.JSON)
                .param("nome", nome)
            .when()
                .get("/usuarios")
            .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("usuarios[0].password");
    }
    @Test
    @Order(7)
    @DisplayName("GET - Listar usuários pelo PASSWORD")
    public void testListarUsuariospeloPassword(){
         given()
                .contentType(ContentType.JSON)
                .param("password", password)
            .when()
                .get("/usuarios")
            .then()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(8)
    @DisplayName("GET - Listar usuários pelo ADMINISTRADOR")
    public void testListarUsuariospeloAdministrador(){

        given()
                .contentType(ContentType.JSON)
                .param("administrador", administrador)
            .when()
                .get("/usuarios")
            .then()
                .assertThat()
                .statusCode(200);

    }
    @Test
    @Order(9)
    @DisplayName("GET - Validar usuário não encontrado")
    public void testValidarUsuarioNaoEncontrado(){

        given()
                .contentType(ContentType.JSON)
                .pathParams("_id", "fakeId123")
            .when()
                .get("/usuarios/{_id}" )
            .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));

    }


}
