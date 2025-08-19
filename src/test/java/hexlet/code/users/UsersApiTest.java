package hexlet.code.users;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersApiTest {

    @LocalServerPort
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @BeforeEach
    void setUp() {
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
                .statusCode(201);

        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                      {"email":"%s","password":"secret"}
                      """.formatted(adminEmail))
                .post("/login")
                .then()
                .statusCode(200)
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
                .statusCode(201);

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get("/users")
                .then()
                .statusCode(200);
    }
}
