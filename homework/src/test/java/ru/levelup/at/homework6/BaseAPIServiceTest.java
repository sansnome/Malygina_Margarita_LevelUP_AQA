package ru.levelup.at.homework6;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import ru.levelup.at.homework6.model.CommentData;
import ru.levelup.at.homework6.model.PostData;
import ru.levelup.at.homework6.model.UserData;
import ru.levelup.at.homework6.service.CommentAPIRequest;
import ru.levelup.at.homework6.service.PostAPIRequest;
import ru.levelup.at.homework6.service.UserAPIRequest;

public class BaseAPIServiceTest {
    private static final String ACCESS_TOKEN = "3bd86531ada6db573154fdfb782c66b5bdb827706a2bc6ed6b7f4b1c6c2a591f";

    static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
            .setBaseUri("https://gorest.co.in")
            .setBasePath("public/v2")
            .setContentType(ContentType.JSON)
            .setAuth(RestAssured.oauth2(ACCESS_TOKEN))
            .log(LogDetail.ALL)
            .build();
    }

    static ResponseSpecification responseSpecificationWithCode200() {
        return new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(200)
            .build();
    }

    static ResponseSpecification responseSpecificationWithCode201() {
        return new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(201)
            .build();
    }

    static ResponseSpecification responseSpecificationWithCode204() {
        return new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(204)
            .build();
    }

    static ResponseSpecification responseSpecificationWithCode404() {
        return new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(404)
            .expectBody("message", Matchers.equalTo("Resource not found"))
            .build();
    }

    public static UserData[] getUsers() {
        return new UserAPIRequest(requestSpecification())
            .getUser()
            .then()
            .spec(responseSpecificationWithCode200())
            .extract()
            .as(UserData[].class);
    }

    public static PostData[] getPosts() {
        return new PostAPIRequest(requestSpecification())
            .getPost()
            .then()
            .spec(responseSpecificationWithCode200())
            .extract()
            .as(PostData[].class);
    }

    public static CommentData[] getComments() {
        return new CommentAPIRequest(requestSpecification())
            .getComment()
            .then()
            .spec(responseSpecificationWithCode200())
            .extract()
            .as(CommentData[].class);
    }

    public static Stream<Integer> userIdDataProvider() {
        var users = getUsers();
        List<Integer> userIds = new ArrayList<>();
        for (UserData user : users) {
            userIds.add(user.getId());
        }

        return Stream.of(userIds.get(0), userIds.get(1), userIds.get(2), userIds.get(3));
    }

    public static Stream<Integer> postIdDataProvider() {
        var posts = getPosts();
        List<Integer> postIds = new ArrayList<>();
        for (PostData post : posts) {
            postIds.add(post.getId());
        }

        return Stream.of(postIds.get(0), postIds.get(1), postIds.get(2), postIds.get(3));
    }

    public static Stream<Integer> commentIdDataProvider() {
        var comments = getComments();
        List<Integer> commentIds = new ArrayList<>();
        for (CommentData comment : comments) {
            commentIds.add(comment.getId());
        }

        return Stream.of(commentIds.get(0), commentIds.get(1), commentIds.get(2), commentIds.get(3));
    }
}
