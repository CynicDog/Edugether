package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.entity.users.User;
import org.example.repository.FollowRepository;
import org.example.repository.FollowRequestRepository;
import org.example.repository.UserRepository;
import org.example.repository.implementation.FollowRepositoryImpl;
import org.example.repository.implementation.FollowRequestRepositoryImpl;
import org.example.repository.implementation.UserRepositoryImpl;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class HttpClientTest {

    private final EntityManagerFactory emf;
    private final UserRepository userRepository;
    private final FollowRequestRepository followRequestRepository;
    private final FollowRepository followRepository;
    private final UserService userService;

    public HttpClientTest() {
        this.emf = Persistence.createEntityManagerFactory("edugether");

        this.userRepository = new UserRepositoryImpl(emf);
        this.followRequestRepository = new FollowRequestRepositoryImpl(emf);
        this.followRepository = new FollowRepositoryImpl(emf);

        this.userService = new UserService(userRepository, followRequestRepository, followRepository);
    }

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {

        vertx.deployVerticle(new EdugetherMainApp())
                .onSuccess(success -> testContext.completeNow())
                .onFailure(failure -> testContext.failNow(failure));
    }

    @Test
    public void registerStudentTest(Vertx vertx, VertxTestContext testContext) throws NoSuchAlgorithmException {

        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        String postfix = String.valueOf(secureRandom.nextInt(9000) + 1000);

        String username = "username_test" + "_" + postfix;

        JsonObject payload = new JsonObject()
                .put("username", username)
                .put("password", "1234")
                .put("email", "email" + "_" + postfix + "@test.com");

        vertx.createHttpClient()
                .request(HttpMethod.POST, 8080, "127.0.0.1", "/signup/student")
                .compose(request -> {
                    request.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    return request.send(Json.encodeToBuffer(payload));
                })
                .compose(response -> response.body())
                .onComplete(result -> {
                    if (result.succeeded()) {
                        User user = userService.getUserByUsername(username);
                        assertEquals(username, user.getUsername());

                        userService.deleteByUsername(username);

                        testContext.completeNow();
                    } else {
                        testContext.failNow(result.cause());
                    }
                })
                .onSuccess(user -> testContext.completeNow())
                .onFailure(testContext::failNow);
    }
}
