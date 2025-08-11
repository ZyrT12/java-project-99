package hexlet.code.users;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsNot.not;

public class UsersApiTest {

    @Test
    void crud() {
        RestAssured.baseURI = "http://localhost:8080";

        var id =
                given().contentType("application/json")
                        .body("""
                    {"email":"jack@google.com","firstName":"Jack","lastName":"Jons","password":"some-password"}
                """)
                        .when().post("/api/users")
                        .then().statusCode(201)
                        .body("email", equalTo("jack@google.com"))
                        .body("$", not(hasKey("password")))
                        .extract().path("id");

        given().when().get("/api/users/" + id)
                .then().statusCode(200)
                .body("email", equalTo("jack@google.com"))
                .body("$", not(hasKey("password")));

        given().when().get("/api/users")
                .then().statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        // UPDATE (partial)
        given().contentType("application/json")
                .body("""
                        {"email":"jack@yahoo.com","password":"new-password"}
                      """)
                .when().put("/api/users/" + id)
                .then().statusCode(200)
                .body("email", equalTo("jack@yahoo.com"));

        given().when().delete("/api/users/" + id)
                .then().statusCode(204);

        given().when().get("/api/users/" + id).then().statusCode(404);
    }

    @Test
    void validation() {
        RestAssured.baseURI = "http://localhost:8080";
        given().contentType("application/json")
                .body("""
                        {"email":"not-email","password":"12"}
                      """)
                .when().post("/api/users")
                .then().statusCode(400);
    }
}
