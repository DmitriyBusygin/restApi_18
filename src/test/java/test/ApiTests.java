package test;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import specs.Specs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

public class ApiTests {

    @Test
    public void logTestGroovy() {
        given().spec(Specs.request)
                .when().get("/users?page=2")
                .then()
                .spec(Specs.responseSpec)
                .body("data.findAll{it.email}.email.flatten()",
                        hasItem("lindsay.ferguson@reqres.in"));
    }

    @Test
    public void listUsersTest() {
        given().spec(Specs.request)
                .when().get("/users?page=2")
                .then()
                .spec(Specs.responseSpec)
                .body("per_page", is(6));
    }

    @Test
    public void createUser() {
        given().spec(Specs.request)
                .body("{\"name\": \"dima\"," +
                        "\"job\": \"QA\"}")
                .when().post("/users")
                .then()
                .statusCode(201)
                .body("name", is("dima"))
                .body("job", is("QA"));
    }

    @Test
    public void register() {
        String token = given().spec(Specs.request)
                .body("{\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"pistol\"}")
                .when().post("/register")
                .then()
                .spec(Specs.responseSpec)
                .extract().path("token");
        StringUtils.isNoneEmpty(token);
    }

    @Test
    void registerUnsuccessful() {
        given().spec(Specs.request)
                .body("{\"email\": \"dima@mail.ru\"}")
                .when().post("/register")
                .then().statusCode(400)
                .body("error", is("Missing password"));
    }
}
