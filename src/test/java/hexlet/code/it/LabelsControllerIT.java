package hexlet.code.it;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LabelsControllerIT {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/labels";
    }

    @Test
    void createAndList() {
        var id = RestAssured.given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"prio\"}")
            .post("")
            .then()
            .statusCode(anyOf(is(201), is(200)))
            .extract().jsonPath().getLong("id");
        RestAssured.get("")
            .then()
            .statusCode(200)
            .body("name", hasItem("prio"));
    }
}
