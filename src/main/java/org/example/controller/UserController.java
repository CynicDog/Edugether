package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.entity.users.User;
import org.example.projection.UserProjection;
import org.example.service.UserService;
import org.example.util.enums.TYPE;
import org.jboss.logging.Logger;

import java.security.Principal;

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

        // http GET http://localhost:8080/my-page
        router.get("/my-page").handler(this::handleMyPage);

        // http -h POST http://localhost:8080/signup/student username=simon password=1234 email=simon@test.com
        router.post("/signup/student").handler(BodyHandler.create()).handler(this::handleStudentSignup);

        // http -h POST http://localhost:8080/signup/teacher username=sammy password=1234 email=sammy@test.com
        router.post("/signup/teacher").handler(BodyHandler.create()).handler(this::handleTeacherSignup);

    }

    private void handleMyPage(RoutingContext routingContext) {

        Principal authenticatedPrincipal = routingContext.session().get("Authentication");

        if (authenticatedPrincipal != null) {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "my-page.html");
        } else {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "login.html");
        }
    }

    private void handleTeacherSignup(RoutingContext routingContext) {

        JsonObject userCommand = routingContext.getBodyAsJson();
        userCommand.put("TYPE", TYPE.TEACHER);

        userService.registerTeacher(userCommand);

        routingContext.response().setStatusCode(200).end();
    }

    private void handleStudentSignup(RoutingContext routingContext) {

        JsonObject userCommand = routingContext.getBodyAsJson();
        userCommand.put("TYPE", TYPE.STUDENT);

        userService.registerStudent(userCommand);

        routingContext.response().setStatusCode(200).end();
    }


}
