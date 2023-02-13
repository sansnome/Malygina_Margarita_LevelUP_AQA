package ru.levelup.at.homework6;

import static  org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.levelup.at.homework6.model.CreateUserDataRequest;
import ru.levelup.at.homework6.model.UserData;
import ru.levelup.at.homework6.model.UserData.Gender;
import ru.levelup.at.homework6.model.UserData.Status;
import ru.levelup.at.homework6.service.UserAPIRequest;

public class UserAPIServiceTest extends BaseAPIServiceTest {
    static Stream<Arguments> userParametersDataProvider() {

        var users = getUsers();

        return Stream.of(
            Arguments.of(Map.of("name", users[new Random().nextInt(5)].getName())),
            Arguments.of(Map.of("email", users[new Random().nextInt(5)].getEmail())),
            Arguments.of(Map.of("gender", Gender.values()[new Random().nextInt(Gender.values().length)].toString())),
            Arguments.of(Map.of("status", Status.values()[new Random().nextInt(Status.values().length)].toString()))
        );
    }

    static Stream<Arguments> parametersForCreatingUsersDataProvider() {

        var faker = new Faker();

        List<Arguments> userParameters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userParameters.add(Arguments.of(
                faker.name().firstName() + " " + faker.name().lastName(),
                faker.internet().emailAddress(),
                Gender.values()[new Random().nextInt(Gender.values().length)].toString(),
                Status.values()[new Random().nextInt(Status.values().length)].toString()
            ));
        }

        return userParameters.stream();
    }

    private UserData[] getUsersByParameters(Map<String, String> params) {
        return new UserAPIRequest(requestSpecification())
            .getUserByParameters(params)
            .then()
            .spec(responseSpecificationWithCode200())
            .extract()
            .as(UserData[].class);
    }

    @Test
    void getUsersTest() {
        var rsBody = new UserAPIRequest(requestSpecification())
            .getUser()
            .then()
            .spec(responseSpecificationWithCode200())
            .extract()
            .as(UserData[].class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(rsBody).extracting(UserData::getId).isNotNull();
            softly.assertThat(rsBody).extracting(UserData::getName).isNotNull();
            softly.assertThat(rsBody).extracting(UserData::getEmail).isNotNull();
            softly.assertThat(rsBody).extracting(UserData::getGender).isNotNull();
            softly.assertThat(rsBody).extracting(UserData::getStatus).isNotNull();
        });
    }

    @ParameterizedTest
    @MethodSource("userParametersDataProvider")
    void getUsersByParameterTest(Map<String, String> parameters) {

        if (parameters.containsKey("name")) {
            var rsBody = getUsersByParameters(parameters);
            assertThat(rsBody[0].getName()).isEqualTo(parameters.get("name"));
        } else if (parameters.containsKey("email")) {
            var rsBody = getUsersByParameters(parameters);
            assertThat(rsBody[0].getEmail()).isEqualTo(parameters.get("email"));
        } else if (parameters.containsKey("gender")) {
            var rsBody = getUsersByParameters(parameters);
            assertThat(rsBody).extracting(UserData::getGender).containsOnly(parameters.get("gender"));
        } else if (parameters.containsKey("status")) {
            var rsBody = getUsersByParameters(parameters);
            assertThat(rsBody).extracting(UserData::getStatus).containsOnly(parameters.get("status"));
        }
    }

    @ParameterizedTest
    @MethodSource("userIdDataProvider")
    void getUserById(int id) {
        var rsBody = new UserAPIRequest(requestSpecification())
            .getUserById(id)
            .then()
            .spec(responseSpecificationWithCode200())
            .extract()
            .as(UserData.class);

        assertThat(rsBody.getId()).isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 22, 55555, 6000006})
    void getNonexistentUserByIdTest(int id) {
        new UserAPIRequest(requestSpecification())
            .getUserById(id)
            .then()
            .spec(responseSpecificationWithCode404());
    }

    @ParameterizedTest
    @MethodSource("parametersForCreatingUsersDataProvider")
    void createUserTest(String name, String email, String gender, String status) {
        var rqBody = CreateUserDataRequest.builder()
                                          .name(name)
                                          .email(email)
                                          .gender(gender)
                                          .status(status)
                                          .build();

        UserData rsBody = new UserAPIRequest(requestSpecification())
            .createUser(rqBody)
            .then()
            .spec(responseSpecificationWithCode201())
            .extract()
            .as(UserData.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(rsBody.getId()).isNotNull();
            softly.assertThat(rsBody.getName()).isEqualTo(rqBody.getName());
            softly.assertThat(rsBody.getEmail()).isEqualTo(rqBody.getEmail());
            softly.assertThat(rsBody.getGender()).isEqualTo(rqBody.getGender());
            softly.assertThat(rsBody.getStatus()).isEqualTo(rqBody.getStatus());
        });
    }

    @ParameterizedTest
    @MethodSource("parametersForCreatingUsersDataProvider")
    void updateUserTest(String name, String email, String gender, String status) {
        var users = getUsers();
        var userId = users[new Random().nextInt(5)].getId();

        var rqBody = CreateUserDataRequest.builder()
                                          .name(name)
                                          .email(email)
                                          .gender(gender)
                                          .status(status)
                                          .build();

        UserData rsBody = new UserAPIRequest(requestSpecification())
            .updateUserById(rqBody, userId)
            .then()
            .spec(responseSpecificationWithCode200())
            .extract()
            .as(UserData.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(rsBody.getId()).isEqualTo(userId);
            softly.assertThat(rsBody.getName()).isEqualTo(rqBody.getName());
            softly.assertThat(rsBody.getEmail()).isEqualTo(rqBody.getEmail());
            softly.assertThat(rsBody.getGender()).isEqualTo(rqBody.getGender());
            softly.assertThat(rsBody.getStatus()).isEqualTo(rqBody.getStatus());
        });
    }

    @ParameterizedTest
    @MethodSource("parametersForCreatingUsersDataProvider")
    void updateNonexistentUserTest(String name, String email, String gender, String status) {
        var userId = new Random().nextInt(888888 + 111111);

        var rqBody = CreateUserDataRequest.builder()
                                          .name(name)
                                          .email(email)
                                          .gender(gender)
                                          .status(status)
                                          .build();

        new UserAPIRequest(requestSpecification())
            .updateUserById(rqBody, userId)
            .then()
            .spec(responseSpecificationWithCode404());
    }

    @ParameterizedTest
    @MethodSource("userIdDataProvider")
    void deleteUserTest(int id) {
        new UserAPIRequest(requestSpecification())
            .deleteUserById(id)
            .then()
            .spec(responseSpecificationWithCode204());
    }

}
