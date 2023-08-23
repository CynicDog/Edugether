package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.entity.academics.Course;
import org.example.entity.users.User;
import org.example.projection.CourseProjection;
import org.example.projection.UserProjection;
import org.example.service.CourseService;
import org.example.service.UserService;
import org.example.util.enums.TYPE;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.example.util.constant.PageLocation.PUBLIC;

public class UserController implements Controller {

    private final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;
    private final CourseService courseService;


    public String[] publicUrls = {"/signup/**", "/login",};

    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        {   // GET handlers
            router.get("/signup").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "signup-entry.html");
            });
            router.get("/signup/student").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "signup-student.html");
            });
            router.get("/signup/teacher").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "signup-teacher.html");
            });
            router.get("/user/check-username").handler(this::handleCheckUsername);
            router.get("/user/check-email").handler(this::handleCheckEmail);
            router.get("/my-page").handler(this::handleMyPage);

            router.get("/teacher").handler(this::handleTeacher);
            router.get("/teacher/course").handler(this::handleTeacherCourse);
        }
        {   // POST handlers
            router.post("/signup/student").handler(BodyHandler.create()).handler(this::handleStudentSignup);
            router.post("/signup/teacher").handler(BodyHandler.create()).handler(this::handleTeacherSignup);
            router.post("/teacher/qualification").handler(BodyHandler.create()).handler(this::handleTeacherQualification);
        }
    }

    private void handleTeacherCourse(RoutingContext routingContext) {

        // for pagination rendering
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("TEACHER")) {
            routingContext.response().setStatusCode(403).end();
            return;
        }

        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<Course> courses = courseService.getPaginatedCoursesByPublishedDateAndByUsernameDescending(
                page.orElse(0),
                limit.orElse(3),
                authentication.getUsername());

        JsonObject data = new JsonObject().put("courses", courses);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    // Authenticated
    private void handleTeacherQualification(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("TEACHER")) {
            routingContext.response().setStatusCode(403).end();
            return;
        }

        JsonObject qualificationCommand = routingContext.getBodyAsJson();
        String qualification = qualificationCommand.getString("name");

        userService.registerQualification(authentication.getUsername(), qualification);

        routingContext.response().setStatusCode(200).setStatusMessage("Successfully registered your qualification :) Reload to see!").end();
    }

    // Authenticated
    private void handleTeacher(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("TEACHER")) {
            routingContext.response().setStatusCode(403).end();
            return;
        }

        User user = userService.getUserByUsername(authentication.getUsername());

        JsonObject userCommand = new JsonObject();
        userCommand.put("user", user);

        routingContext.response().setStatusCode(200).end(userCommand.encode());
    }

    // Authenticated
    private void handleMyPage(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            routingContext.response().setStatusCode(401).end();
            return;
        }

        if (authentication.getType().toString().equals("TEACHER")) {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "teacher.html");
        } else if (authentication.getType().toString().equals("STUDENT")) {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "student.html");
        }
    }

    private void handleCheckUsername(RoutingContext routingContext) {

        String username = routingContext.request().getParam("username");

        if (userService.isUsernameUnique(username)) {
            routingContext.response().setStatusCode(200).end();
        } else {
            routingContext.response().setStatusCode(400).end();
        }
    }

    private void handleCheckEmail(RoutingContext routingContext) {
        String email = routingContext.request().getParam("email");

        if (userService.isEmailUnique(email)) {
            routingContext.response().setStatusCode(200).end();
        } else {
            routingContext.response().setStatusCode(400).end();
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
