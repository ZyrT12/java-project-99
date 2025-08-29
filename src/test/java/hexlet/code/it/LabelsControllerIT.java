package hexlet.code.it;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LabelsControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";

        String email = "it@example.com";
        String pass = "pass";

        userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setPasswordHash(passwordEncoder.encode(pass));
            u.setFirstName("Test");
            u.setLastName("User");
            return userRepository.save(u);
        });

        token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + pass + "\"}")
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Test
    void createAndList() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("{\"name\":\"bug\"}")
                .post("/labels")
                .then()
                .statusCode(201);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/labels")
                .then()
                .statusCode(200);
    }
}
