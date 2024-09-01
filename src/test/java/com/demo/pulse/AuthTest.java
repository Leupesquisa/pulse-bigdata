package com.demo.pulse;

import com.demo.pulse.dto.LoginRequestDTO;
import com.demo.pulse.dto.RegisterRequestDTO;
import com.demo.pulse.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = 8080;
        userRepository.deleteAll(); // Limpa o banco de dados antes de cada teste
    }

    @Test
    void testRegisterUser() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("newuser", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(registerRequestDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("message", equalTo("User registered successfully"))
                .body("data", notNullValue()); // Verifica se o token JWT é retornado
    }

    @Test
    void testRegisterUserWithExistingUsername() {
        // Pré-registra um usuário no banco de dados
        RegisterRequestDTO existingUserDTO = new RegisterRequestDTO("existinguser", "password123");

        // Registrando o usuário pela primeira vez
        given()
                .contentType(ContentType.JSON)
                .body(existingUserDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201); // Verifica se o primeiro registro foi bem-sucedido

        // Tentando registrar novamente com o mesmo nome de usuário
        given()
                .contentType(ContentType.JSON)
                .body(existingUserDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(409)
                .body("message", equalTo("Username already taken"));
    }

    @Test
    void testAuthenticateUser() {
        // Primeiro, registre o usuário para que possamos testá-lo
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("testuser", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(registerRequestDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        // Agora, autentique o usuário registrado
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("testuser", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(loginRequestDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("message", equalTo("Authentication successful"))  // Verifica a mensagem correta
                .body("data", notNullValue()); // Verifica se o token JWT é retornado
    }

    @Test
    void testAuthenticateUserWithInvalidCredentials() {
        // Primeiro, registre o usuário para que possamos testá-lo
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("testuser", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(registerRequestDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        // Agora, tente autenticar com credenciais inválidas
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("testuser", "wrongpassword");

        given()
                .contentType(ContentType.JSON)
                .body(loginRequestDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("message", equalTo("Invalid username or password"));
    }
}
