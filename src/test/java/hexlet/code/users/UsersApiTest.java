package hexlet.code.users;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UsersApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void crud() {
        String adminEmail = "admin+" + UUID.randomUUID() + "@example.com";
        String userEmail = "jack+" + UUID.randomUUID() + "@yahoo.com";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                      {"email":"%s","password":"secret","firstName":"Admin","lastName":"User"}
                      """.formatted(adminEmail))
                .post("/users")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                      {"email":"%s","password":"secret"}
                      """.formatted(adminEmail))
                .post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("token");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                      {"email":"%s","password":"p@ss","firstName":"Jack","lastName":"Smith"}
                      """.formatted(userEmail))
                .post("/users")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get("/users")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
