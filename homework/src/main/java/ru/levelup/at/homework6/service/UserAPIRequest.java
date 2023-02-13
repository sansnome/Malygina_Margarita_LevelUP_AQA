package ru.levelup.at.homework6.service;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import ru.levelup.at.homework6.model.CreateUserDataRequest;

public class UserAPIRequest {

    private static final String USERS_ENDPOINT = "/users";
    private final RequestSpecification requestSpecification;

    public UserAPIRequest(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public Response getUser() {
        return given()
            .spec(requestSpecification)
            .when()
            .get(USERS_ENDPOINT)
            .andReturn();
    }

    public Response createUser(CreateUserDataRequest body) {
        return given()
            .spec(requestSpecification)
            .body(body)
            .when()
            .post(USERS_ENDPOINT)
            .andReturn();
    }

    public Response getUserByParameters(final Map<String, String> parameters) {
        return given()
            .spec(requestSpecification)
            .queryParams(parameters)
            .when()
            .get(USERS_ENDPOINT)
            .andReturn();
    }

    public Response getUserById(int id) {
        return given()
            .spec(requestSpecification)
            .when()
            .get(USERS_ENDPOINT + "/" + id)
            .andReturn();
    }

    public Response updateUserById(CreateUserDataRequest body, int id) {
        return given()
            .spec(requestSpecification)
            .body(body)
            .when()
            .put(USERS_ENDPOINT + "/" + id)
            .andReturn();
    }

    public Response deleteUserById(int id) {
        return given()
            .spec(requestSpecification)
            .when()
            .delete(USERS_ENDPOINT + "/" + id)
            .andReturn();
    }
}
