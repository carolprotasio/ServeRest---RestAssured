package dataFactory;

import pojo.UsuarioPojo;
import com.github.javafaker.Faker;

public class UsuarioDataFactory {

    private static final Faker faker = new Faker();

    public static UsuarioPojo registerUserEmailJaCadastrado() {
        UsuarioPojo loginEmailCadastrado = new UsuarioPojo();
        loginEmailCadastrado.setNome(faker.name().firstName());
        loginEmailCadastrado.setEmail("carol@qa.com");
        loginEmailCadastrado.setPassword("123456");
        loginEmailCadastrado.setAdministrador("false");

        return  loginEmailCadastrado;
    }
    public static UsuarioPojo registerNoAdm() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome(faker.name().firstName());
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setPassword(faker.internet().password());
        newUser.setAdministrador("false");
        return newUser;
    }

    public static UsuarioPojo registerNewUser() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome("Cteste");
        newUser.setEmail("ctest@qa.com");
        newUser.setPassword("123456");
        newUser.setAdministrador("true");
        return newUser;
    }
    public static UsuarioPojo registerUserInvalidEmail() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome(faker.name().firstName());
        newUser.setEmail("invalidEmail.com");
        newUser.setPassword(faker.internet().password());
        newUser.setAdministrador("true");
        return newUser;
    }
    public static UsuarioPojo registerUserEmptyPassword() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome(faker.name().firstName());
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setPassword("");
        newUser.setAdministrador("true");
        return newUser;
    }
    public static UsuarioPojo registerUserEmptyName() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome("");
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setPassword(faker.internet().password());
        newUser.setAdministrador("true");
        return newUser;
    }
    public static UsuarioPojo registerUserEmptyAdmin() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome(faker.name().firstName());
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setPassword(faker.internet().password());
        newUser.setAdministrador("");
        return newUser;
    }
    public static UsuarioPojo registerUserEmptyEmail() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome(faker.name().firstName());
        newUser.setEmail("");
        newUser.setPassword(faker.internet().password());
        newUser.setAdministrador("true");
        return newUser;
    }
    public static UsuarioPojo registerUserEmptyAllData() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome("");
        newUser.setEmail("");
        newUser.setPassword("");
        newUser.setAdministrador("");
        return newUser;
    }
    public static UsuarioPojo userEdit() {
        UsuarioPojo newUser = new UsuarioPojo();
        newUser.setNome(faker.name().firstName());
        newUser.setEmail(faker.name().firstName() + "@qa.com");
        newUser.setPassword(faker.internet().password());
        newUser.setAdministrador("true");
        return newUser;
    }

}
