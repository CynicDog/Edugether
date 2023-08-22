package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.projection.CourseProjection;
import org.example.projection.UserProjection;
import org.example.service.CourseService;
import org.example.service.UserService;
import org.example.util.enums.SUBJECT_TITLE;
import org.jboss.logging.Logger;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
            router.get("/course").handler(routingContext -> { routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "course.html"); });
            router.get("/course/subjects").handler(this::handleCourseSubjects);
            router.get("/course/newest").handler(this::handleCourseNewest);
        }
        {   // POST handlers
            router.post("/course/register").handler(BodyHandler.create()).handler(this::handleCourseRegister);
        }
    }

    // Authenticated
    private void handleCourseNewest(RoutingContext routingContext) {

        // for pagination rendering
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<CourseProjection> newestCourses = courseService.getPaginatedCoursesByPublishedDateDescending(
                page.orElse(0),
                limit.orElse(9));

        JsonObject data = new JsonObject().put("courses", newestCourses);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    // Authenticated
    private void handleCourseRegister(RoutingContext routingContext)  {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("TEACHER")) {
            logger.info("[ Unauthorized ]");
            routingContext.response().setStatusCode(403).end();
            return;
        }

        JsonObject courseCommand = routingContext.getBodyAsJson();
        courseCommand.put("teacher", authentication.getUsername());

        if (courseService.registerCourse(courseCommand)) {
            routingContext.response().setStatusCode(200).setStatusMessage("Successfully registered!").end();
        } else {
            routingContext.response().setStatusCode(400).setStatusMessage("Something went wrong. You might want to check your inputs again.").end();
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
