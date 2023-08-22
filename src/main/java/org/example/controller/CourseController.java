package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.entity.users.Teacher;
import org.example.service.CourseService;
import org.example.service.UserService;
import org.example.util.enums.SUBJECT_TITLE;
import org.jboss.logging.Logger;

import java.security.Principal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.util.constant.PageLocation.PUBLIC;

public class CourseController implements Controller {

    private final String signingKey;
    private final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;
    private final CourseService courseService;

    public CourseController(UserService userService, CourseService courseService, String signingKey) {
        this.userService = userService;
        this.courseService = courseService;
        this.signingKey = signingKey;
    }

    @Override
    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        {   // GET handlers
            router.get("/course").handler(routingContext -> { routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "course-entry.html"); });
            router.get("/course/subjects").handler(this::handleCourseSubjects);
        }
        {   // POST handlers
            router.post("/course/register").handler(BodyHandler.create()).handler(this::handleCourseRegister);
        }
    }

    // Authenticated
    private void handleCourseRegister(RoutingContext routingContext) {

        Principal authenticatedPrincipal = routingContext.session().get("Authentication");

        if (authenticatedPrincipal == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(302).end();

            return;
        }

        JsonObject courseCommand = routingContext.getBodyAsJson();
        courseCommand.put("teacher", authenticatedPrincipal.getName());

        if (courseService.registerCourse(courseCommand)) {
            routingContext.response().setStatusCode(200).setStatusMessage("Successfully registered!").end();
        } else {
            logger.info("failed");
            routingContext.response().setStatusCode(400).setStatusMessage("Something went wrong :(").end();
        }
    }

    private void handleCourseSubjects(RoutingContext routingContext) {

        List<String> subjectNames =
                Arrays.stream(SUBJECT_TITLE.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());

        JsonObject data = new JsonObject().put("subjectNames", subjectNames);

        routingContext.response().setStatusCode(200).end(data.encode());
    }
}
