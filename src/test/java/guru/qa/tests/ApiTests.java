package guru.qa.tests;

import guru.qa.models.lombok.JobBodyModel;
import guru.qa.models.lombok.JobResponseModel;
import guru.qa.models.pojo.RegistrationBodyModel;
import guru.qa.models.pojo.RegistrationResponseModel;
import guru.qa.models.pojo.RegistrationUnsuccessfulResponseModel;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.specs.JobSpec.*;
import static guru.qa.specs.RegistrationSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTests extends TestBase {

    @Epic("Регистрация")
    @DisplayName("Регистрация пользователя")
    @Test
    void registrationSuccessfulTest() {
        RegistrationBodyModel registrationBody = new RegistrationBodyModel();
        registrationBody.setEmail("eve.holt@reqres.in");
        registrationBody.setPassword("pistol");

        RegistrationResponseModel response = step("Зарегистрировать пользователя", () ->
                given(registrationRequestSpec)
                        .body(registrationBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registrationResponseSpec)
                        .extract().as(RegistrationResponseModel.class));

        step("Проверить response", () -> {
            assertEquals(4, response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }

    @Epic("Регистрация")
    @DisplayName("Ошибка 'Missing password' при не заполнении пароля")
    @Test
    void registrationUnsuccessfulTest() {
        RegistrationBodyModel registrationBody = new RegistrationBodyModel();
        registrationBody.setEmail("sydney@fife");

        RegistrationUnsuccessfulResponseModel response = step("Зарегистрировать пользователя", () ->
                given(registrationRequestSpec)
                        .body(registrationBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registrationUnsuccessfulResponseSpec)
                        .extract().as(RegistrationUnsuccessfulResponseModel.class));

        step("Проверить response", () ->
                assertEquals("Missing password", response.getError()));
    }

    @Epic("Job")
    @DisplayName("Создать Job")
    @Test
    void createTest() {
        JobBodyModel jobBody = new JobBodyModel();
        jobBody.setName("morpheus");
        jobBody.setJob("leader");

        JobResponseModel response = step("Создать Job", () ->
                given(jobRequestSpec)
                        .body(jobBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createJobResponseSpec)
                        .extract().as(JobResponseModel.class));

        step("Проверить response", () -> {
            assertEquals("morpheus", response.getName());
            assertEquals("leader", response.getJob());
        });
    }

    @Epic("Job")
    @DisplayName("Обновить Job")
    @Test
    void updateTest() {
        JobBodyModel jobBody = new JobBodyModel();
        jobBody.setName("morpheus");
        jobBody.setJob("zion resident");

        JobResponseModel response = step("Обновить Job", () ->
                given(jobRequestSpec)
                        .body(jobBody)
                        .when()
                        .post("/users/2")
                        .then()
                        .spec(createJobResponseSpec)
                        .extract().as(JobResponseModel.class));

        step("Проверить response", () -> {
            assertEquals("morpheus", response.getName());
            assertEquals("zion resident", response.getJob());
        });
    }

    @Epic("Job")
    @DisplayName("Удалить Job")
    @Test
    void deleteTest() {
        step("Удалить Job", () -> given(jobRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(deleteJobResponseSpec));
    }
}
