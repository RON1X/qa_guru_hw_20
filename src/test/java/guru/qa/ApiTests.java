package guru.qa;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ApiTests extends TestBase {
    @Test
    void registrationSuccessfulTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}")
                .contentType(JSON)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void registrationUnsuccessfulTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body("{ \"email\": \"sydney@fife\"}")
                .contentType(JSON)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void createTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\"}")
                .contentType(JSON)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body("{ \"name\": \"morpheus\", \"job\": \"zion resident\"}")
                .contentType(JSON)
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

    @Test
    void deleteTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .delete("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
