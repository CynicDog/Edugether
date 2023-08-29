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
import org.example.entity.users.Student;
import org.example.projection.CourseProjection;
import org.example.projection.ReviewProjection;
import org.example.projection.UserProjection;
import org.example.service.CourseService;
import org.example.service.UserService;
import org.example.util.enums.REVIEW_SENTIMENT;
import org.example.util.enums.SUBJECT_TITLE;
import org.jboss.logging.Logger;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.example.util.constant.PageLocation.PUBLIC;

public class CourseController implements Controller {
    private final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;
    private final CourseService courseService;

    public CourseController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        {   // GET handlers
            router.get("/course").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "course.html");
            });
            router.get("/course/subjects").handler(this::handleCourseSubjects);
            router.get("/courses").handler(this::handleCourses);
            router.get("/course/details-page").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "course-details.html");
            });
            
            router.get("/review/sentiments").handler(this::handleReviewSentiments);
            router.get("/course/details").handler(this::handleCourseDetails);
            router.get("/course/reviews").handler(this::handleCourseReviews);
        }
        {   // POST handlers
            router.post("/course/register").handler(BodyHandler.create()).handler(this::handleCourseRegister);
            router.post("/course/enroll").handler(BodyHandler.create()).handler(this::handleCourseEnroll);
            router.post("/course/wish").handler(BodyHandler.create()).handler(this::handleCourseWish);
            router.post("/review/register").handler(BodyHandler.create()).handler(this::handleReviewRegister);
            router.post("/review/like").handler(this::handleReviewLike);
        }
    }

    private void handleReviewLike(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        }

        Long reviewId = Long.parseLong(routingContext.request().getParam("reviewId"));

        try {
            BigInteger likedCount = courseService.registerLike(reviewId, authentication.getId());
            routingContext.response().setStatusCode(200).setStatusMessage("Liked the review, awesome!").end(String.valueOf(likedCount));
        } catch (Exception e) {
            routingContext.response().setStatusCode(500).setStatusMessage(e.getMessage()).end();
        }
    }

    private void handleCourseReviews(RoutingContext routingContext) {

        // for pagination rendering
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long courseId = Long.parseLong(routingContext.request().getParam("courseId"));
        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<ReviewProjection> reviews = courseService.getPaginatedReviewsByCourseIdAndCreateDateDescending(courseId, page.orElse(0), limit.orElse(4));

        JsonObject data = new JsonObject().put("reviews", reviews);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleCourseDetails(RoutingContext routingContext) {

        Long courseId = Long.parseLong(routingContext.request().getParam("id"));
        Course course = courseService.getCourseById(courseId);

        JsonObject data = new JsonObject();
        data.put("course", course);

        List<Student> participants = courseService.getStudentsByRegistration_CourseId(courseId);
        data.put("participants", participants);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    // Authenticated
    private void handleReviewRegister(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("STUDENT")) {
            logger.info("[ Unauthorized ]");
            routingContext.response().setStatusCode(403).end();
            return;
        }

        JsonObject reviewCommand = routingContext.getBodyAsJson();
        reviewCommand.put("studentId", authentication.getId());

        try {
            courseService.registerReview(reviewCommand);
            routingContext.response().setStatusCode(200).setStatusMessage("Successfully registered! Reload to see yours.").end();
        } catch (IllegalStateException e) {
            routingContext.response().setStatusCode(500).setStatusMessage(e.getMessage()).end();
        } catch (Exception e) {
            routingContext.response().setStatusCode(500).setStatusMessage("Something went wrong.. You might want to check your inputs again.").end();
        }
    }

    private void handleReviewSentiments(RoutingContext routingContext) {

        List<String> subjectNames = Arrays.stream(REVIEW_SENTIMENT.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList());

        JsonObject data = new JsonObject().put("sentimentNames", subjectNames);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    // Authenticated
    private void handleCourseEnroll(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("STUDENT")) {
            logger.info("[ Unauthorized ]");
            routingContext.response().setStatusCode(403).end();
            return;
        }

        Long courseId = Long.parseLong(routingContext.request().getParam("courseId"));

        try {
            courseService.enrollOnCourse(authentication.getUsername(), courseId);
            routingContext.response().setStatusCode(200).setStatusMessage("Successfully enrolled on the course!").end();
        } catch (Exception e) {
            routingContext.response().setStatusCode(500).setStatusMessage("Already enrolled on.").end();
        }
    }

    // Authenticated
    private void handleCourseWish(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("STUDENT")) {
            logger.info("[ Unauthorized ]");
            routingContext.response().setStatusCode(403).end();
            return;
        }

        Long courseId = Long.parseLong(routingContext.request().getParam("courseId"));

        try {
            courseService.wishForCourse(authentication.getUsername(), courseId);
            routingContext.response().setStatusCode(200).setStatusMessage("Successfully wished for the course!").end();
        } catch (Exception e) {
            routingContext.response().setStatusCode(500).setStatusMessage(e.getMessage()).end();
        }
    }

    private void handleCourses(RoutingContext routingContext) {

        // for pagination rendering
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Optional<String> option = Optional.of(routingContext.request().getParam("option"));
        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<CourseProjection> courses = courseService.getPaginatedCoursesByOption(
                option.orElse("newest"),
                page.orElse(0),
                limit.orElse(9));

        JsonObject data = new JsonObject().put("courses", courses);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    // Authenticated
    private void handleCourseRegister(RoutingContext routingContext) {

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

        try {
            courseService.registerCourse(courseCommand);
            routingContext.response().setStatusCode(200).setStatusMessage("Successfully registered!").end();
        } catch (Exception e) {
            routingContext.response().setStatusCode(500).setStatusMessage("Something went wrong :( You might want to check your inputs again.").end();
        }
    }

    private void handleCourseSubjects(RoutingContext routingContext) {

        List<String> subjectNames = Arrays.stream(SUBJECT_TITLE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList());

        JsonObject data = new JsonObject().put("subjectNames", subjectNames);

        routingContext.response().setStatusCode(200).end(data.encode());
    }
}
