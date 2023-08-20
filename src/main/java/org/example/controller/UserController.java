package org.example.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.service.UserService;
import org.example.util.enums.TYPE;
import org.jboss.logging.Logger;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static org.example.util.constant.PageLocation.PUBLIC;

public class UserController implements Controller {

    private final String signingKey;
    private final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;
    public String[] publicUrls = {"/signup/**", "/login",};

    public UserController(UserService userService, String signingKey) {
        this.userService = userService;
        this.signingKey = signingKey;
    }

    @Override
    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        // http GET http://localhost:8080/signup
        router.get("/signup").handler(routingContext -> {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "signup.html");
        });

        // http GET http://localhost:8080/login
        router.get("/login").handler(routingContext -> {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "login.html");
        });

        router.get("/my-page").handler(routingContext -> {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "my-page.html");
        });

        // http -h POST http://localhost:8080/my-page Authorization:{jwt}
        router.post("/my-page").handler(this::handleMyPage);

        // http -h POST http://localhost:8080/signup/student username=simon password=1234 email=simon@test.com
        router.post("/signup/student").handler(BodyHandler.create()).handler(this::handleStudentSignup);

        // http -h POST http://localhost:8080/signup/teacher username=sammy password=1234 email=sammy@test.com
        router.post("/signup/teacher").handler(BodyHandler.create()).handler(this::handleTeacherSignup);

        // http -h POST http://localhost:8080/login username=sammy password=1234
        router.post("/login").handler(BodyHandler.create()).handler(this::handleLogin);
    }

    private void handleMyPage(RoutingContext routingContext) {

        String jwt = Optional.ofNullable(routingContext.request().getHeader("Authorization")).orElse(null);

        if (jwt != null) {
            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

            String username = String.valueOf(claims.get("username"));
            logger.info(username);

            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "text/html")
                    .putHeader("Location", "/my-page")
                    .end();
        } else {
            routingContext.response()
                    .setStatusCode(400)
                    .setStatusMessage("to Authentication entry point..")
                    .end();
        }
    }

    private void handleTeacherSignup(RoutingContext routingContext) {

        JsonObject userCommand = routingContext.getBodyAsJson();
        userCommand.put("TYPE", TYPE.TEACHER);

        userService.registerTeacher(userCommand);

        routingContext.response().setStatusCode(200).putHeader("PageLocation", "/login").end();
    }

    private void handleStudentSignup(RoutingContext routingContext) {

        JsonObject userCommand = routingContext.getBodyAsJson();
        userCommand.put("TYPE", TYPE.STUDENT);

        userService.registerStudent(userCommand);

        routingContext.response().setStatusCode(200).putHeader("PageLocation", "/login").end();
    }

    private void handleLogin(RoutingContext routingContext) {

        JsonObject credentials = routingContext.getBodyAsJson();

        if (userService.authenticate(credentials)) {
            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setClaims(Map.of("username", credentials.getString("username"))).signWith(key).compact();

            routingContext.response().setStatusCode(200).putHeader("Authorization", jwt).putHeader("Location", "/home").end();
        } else {
            routingContext.response().setStatusCode(400).setStatusMessage("Bad credentials!").end();
        }
    }
}
