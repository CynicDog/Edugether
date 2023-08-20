package org.example.controller;

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
        {   // GET handlers
            router.get("/signup").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "signup.html");
            });

            router.get("/my-page").handler(this::handleMyPage);
        }
        {   // POST handlers
            router.post("/signup/student").handler(BodyHandler.create()).handler(this::handleStudentSignup);
            router.post("/signup/teacher").handler(BodyHandler.create()).handler(this::handleTeacherSignup);
        }

    }

    // Authenticated
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
