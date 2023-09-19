package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.entity.users.User;
import org.example.service.CourseService;
import org.example.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class HttpClientTest {
    private final UserService userService;
    private final CourseService courseService;

    public HttpClientTest() throws NoSuchAlgorithmException {

        // static dependency injection pattern
        this.userService = EdugetherMainApp.getUserService();
        this.courseService = EdugetherMainApp.getCourseService();
    }

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {

        vertx.deployVerticle(new EdugetherMainApp())
                .onSuccess(success -> testContext.completeNow())
                .onFailure(failure -> testContext.failNow(failure));
    }

    String studentUsername = "test_studentUsername";
    String teacherUsername = "test_teacherUsername";

    @Test
    public void registerStudentTest(Vertx vertx, VertxTestContext testContext) {
        JsonObject payload = new JsonObject().put("username", "test_studentUsername").put("password", "1234").put("email", "test_studentEmail@test.com");

        vertx.createHttpClient().request(HttpMethod.POST, 8080, "127.0.0.1", "/signup/student")
                .compose(request -> {
                    request.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    return request.send(Json.encodeToBuffer(payload));
                }).onComplete(result -> {
                    if (result.succeeded()) {
                        User user = userService.getUserByUsername(studentUsername);
                        assertEquals(studentUsername, user.getUsername());

                        userService.deleteByUsername(studentUsername);
                        testContext.completeNow();
                    } else {
                        testContext.failNow(result.cause());
                    }
                }).onSuccess(user -> testContext.completeNow()).onFailure(testContext::failNow);
    }

    @Test
    public void fetchStudentsRandomly(Vertx vertx, VertxTestContext testContext) {

        vertx.createHttpClient().request(HttpMethod.GET, 8080, "127.0.0.1", "/user/fetch-randomly?limit=7")
                .compose(request -> request.send())
                .compose(response -> response.body())
                .onComplete(result -> {
                    if (result.succeeded()) {
                        JsonObject jsonResponse = result.result().toJsonObject();
                        JsonArray usersArray = jsonResponse.getJsonArray("users");

                        assertEquals(7, usersArray.size());
                        testContext.completeNow();
                    } else {
                        testContext.failNow(result.cause());
                    }
        });
    }

    @Test
    public void checkUsernameUniqueness(Vertx vertx, VertxTestContext testContext) {

        JsonObject payload = new JsonObject().put("username", studentUsername).put("password", "1234").put("email", "test_studentEmail@test.com");

        vertx.createHttpClient().request(HttpMethod.POST, 8080, "127.0.0.1", "/signup/student")
                .compose(signupRequest -> {
                    signupRequest.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    return signupRequest.send(Json.encodeToBuffer(payload));
                }).onComplete(signupResponse -> {

                    vertx.createHttpClient().request(HttpMethod.GET, 8080, "127.0.0.1", "/user/check-username?username=" + studentUsername)
                            .compose(checkUsernameRequest -> checkUsernameRequest.send())
                            .compose(checkUsernameResponse -> {
                                assertEquals(400, checkUsernameResponse.statusCode());

                                return checkUsernameResponse.end();
                            })
                            .onComplete(asyncResult -> {
                                userService.deleteByUsername(studentUsername);
                            })
                            .onSuccess(user -> testContext.completeNow())
                            .onFailure(testContext::failNow);
                });
    }

    @Test
    public void login(Vertx vertx, VertxTestContext testContext) {

        JsonObject signupPayload = new JsonObject()
                .put("username", teacherUsername)
                .put("password", "1234")
                .put("email", "test_teacherEmail@test.com");

        vertx.createHttpClient().request(HttpMethod.POST, 8080, "127.0.0.1", "/signup/teacher")
                .compose(signupRequest -> {
                    signupRequest.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    return signupRequest.send(Json.encodeToBuffer(signupPayload));
                }).onComplete(signupResponse -> {

                    JsonObject loginPayload = new JsonObject()
                            .put("username", teacherUsername)
                            .put("password", "1234");

                    vertx.createHttpClient().request(HttpMethod.POST, 8080, "127.0.0.1", "/login")
                            .compose(loginRequest -> {
                                loginRequest.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                                return loginRequest.send(Json.encodeToBuffer(loginPayload));
                            })
                            .compose(loginResponse -> {
                                assertEquals(200, loginResponse.statusCode());
                                assertNotEquals(401, loginResponse.statusCode());

                                return loginResponse.end();
                            }).onComplete(asyncResult -> {
                                userService.deleteByUsername(teacherUsername);
                            })
                            .onSuccess(user -> testContext.completeNow())
                            .onFailure(testContext::failNow);
                });
    }
}
