package modulos.usuarios;

import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Validação da funcionalidade => Cadastro de Usuário")
public class UsuarioCasoDeTest {

    private String userId;
    private String nome;
    private String email;
    private String password;
    private String administrador;

    @BeforeEach
    public void beforeEach() {
        baseURI = "http://localhost";
        port = 3000;
    }
    @Test
    @Order(1)
    @DisplayName("CT-001: Cadastrar novo usuário com dados válidos com sucesso")
    public void testCadastrarDadosValidos() {

         userId = given()
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
    @DisplayName("CT-002: Cadastrar novo usuário com Email Inválido - sem @")
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
    @DisplayName("CT-003: Cadastrar novo usuário com campo da EMAIL vazio")
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
    @DisplayName("CT-004: Cadastrar novo usuário com campo da SENHA vazio")
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
    @DisplayName("CT-005: Cadastrar novo usuário com campo da NOME vazio")
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
    @DisplayName("CT-006: Cadastrar novo usuário com campo da ADMIN vazio")
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
    @DisplayName("CT-007: Cadastrar novo usuário com TODOS campos vazio")
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
    @DisplayName("CT-008: Cadastrar novo usuário com EMAIL já cadastrado")
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
    @Test
    @Order(9)
    @DisplayName("CT-009: Listar usuários cadastrados")
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
    @Order(10)
    @DisplayName("CT-010: Listar usuário pelo ID")
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
    @Order(11)
    @DisplayName("CT-011: Listar usuários pelo EMAIL")
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

    }
    @Test
    @Order(12)
    @DisplayName("CT-012: Listar usuários pelo NOME")
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
    @Order(13)
    @DisplayName("CT-013: Listar usuários pelo PASSWORD")
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
    @Order(14)
    @DisplayName("CT-014: Listar usuários pelo ADMINISTRADOR")
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
    @Order(15)
    @DisplayName("CT-015: Validar usuário não encontrado")
    public void testValidarUsuarioNaoEncontrado(){

        given()
                .contentType(ContentType.JSON)
                .pathParams("_id", "fakeId123")
                .when()
                .get("/usuarios/{_id}" )
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }
    @Test
    @Order(16)
    @DisplayName("CT-016: Alterar dados de um usuário com o ID ")
    public void testEditarUsuario() {

        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.userEdit())
                .when()
                .put("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("message", equalTo("Registro alterado com sucesso"))
                .statusCode(200);

    }
    @Test
    @Order(17)
    @DisplayName("CT-017: Alterar dados de um usuário com campo NOME vazio")
    public void testEditarUsuarioCampoNomeVazio(){
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.alterarNomeVazio())
                .when()
                .put("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("nome", equalTo("nome não pode ficar em branco"))
                .statusCode(400);
    }
    @Test
    @Order(18)
    @DisplayName("CT-018: Alterar dados de um usuário com campo EMAIL vazio")
    public void testEditarUsuarioCampoEmailVazio(){
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.alterarEmailVazio())
                .when()
                .put("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("email", equalTo("email não pode ficar em branco"))
                .statusCode(400);
    }
    @Test
    @Order(19)
    @DisplayName("CT-019: Alterar dados de um usuário com campo PASSWORD vazio")
    public void testEditarUsuarioCampoPasswordVazio(){
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.alterarPasswordVazio())
                .when()
                .put("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("password", equalTo("password não pode ficar em branco"))
                .statusCode(400);
    }
    @Test
    @Order(20)
    @DisplayName("CT-020: Alterar dados de um usuário com campo ADMINISTRADOR vazio")
    public void testEditarUsuarioCampoAdmVazio(){
        given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.alterarAdmVazio())
                .when()
                .put("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"))
                .statusCode(400);
    }
    @Test
    @Order(21)
    @DisplayName("CT-021: Excluir um usuário com ID inexistente")
    public void testExcluirUsuarioInexistente() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/usuarios/" + "inexistente")
                .then()
                .assertThat()
                .body("message", equalTo("Nenhum registro excluído"))
                .statusCode(200);
    }
    @Test
    @Order(22)
    @DisplayName("CT-022: Excluir um usuário com ID existente")
    public void testExcluirUsuarioExistente() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("message", equalTo("Registro excluído com sucesso"))
                .statusCode(200);
    }
    @Test
    @Order(23)
    @DisplayName("CT-023: Excluir um usuário previamente excluído")
    public void testExcluirUsuariojaExcluido() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/usuarios/" + userId)
                .then()
                .assertThat()
                .body("message", equalTo("Nenhum registro excluído"))
                .statusCode(200);
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
                .body("message", equalTo("Nenhum registro excluído"))
                .statusCode(200);
    }

}
