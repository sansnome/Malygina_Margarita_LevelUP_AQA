package ru.levelup.at.homework6.service;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import ru.levelup.at.homework6.model.CreateCommentDataRequest;


public class CommentAPIRequest {
    private  static final String COMMENTS_ENDPOINT = "/comments";

    private final RequestSpecification requestSpecification;

    public CommentAPIRequest(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public Response getComment() {
        return given()
            .spec(requestSpecification)
            .when()
            .get(COMMENTS_ENDPOINT)
            .andReturn();
    }

    public Response createComment(CreateCommentDataRequest body) {
        return given()
            .spec(requestSpecification)
            .body(body)
            .when()
            .post(COMMENTS_ENDPOINT)
            .andReturn();
    }

    public Response getCommentByParameters(final Map<String, String> parameters) {
        return given()
            .spec(requestSpecification)
            .queryParams(parameters)
            .when()
            .get(COMMENTS_ENDPOINT)
            .andReturn();
    }

    public Response getCommentById(int id) {
        return given()
            .spec(requestSpecification)
            .when()
            .get(COMMENTS_ENDPOINT + "/" + id)
            .andReturn();
    }

    public Response updateCommentById(CreateCommentDataRequest body, int id) {
        return given()
            .spec(requestSpecification)
            .body(body)
            .when()
            .put(COMMENTS_ENDPOINT + "/" + id)
            .andReturn();
    }

    public Response deleteCommentById(int id) {
        return given()
            .spec(requestSpecification)
            .when()
            .delete(COMMENTS_ENDPOINT + "/" + id)
            .andReturn();
    }
}
