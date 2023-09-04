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
import org.example.entity.academics.Review;
import org.example.entity.users.User;
import org.example.projection.CourseProjection;
import org.example.projection.RegistrationProjection;
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
            router.get("/student").handler(this::handleStudent);
            router.get("/student/enrolled-course").handler(this::handleStudentEnrolledCourse);
            router.get("/student/wished-course").handler(this::handleStudentWishedCourse);
            router.get("/student/review").handler(this::handleStudentReview);
            router.get("/user-details").handler(this::handleUserDetails);

            router.get("/socials").handler(this::handleSocials);
            router.get("/follow/request").handler(this::handleFollowRequestGet);
            router.get("/followers-count").handler(this::handleFollowersCount);
            router.get("/followings-count").handler(this::handleFollowingsCount);
        }
        {   // POST handlers
            router.post("/signup/student").handler(BodyHandler.create()).handler(this::handleStudentSignup);
            router.post("/signup/teacher").handler(BodyHandler.create()).handler(this::handleTeacherSignup);
            router.post("/teacher/qualification").handler(BodyHandler.create()).handler(this::handleTeacherQualification);
            router.post("/student/interest").handler(BodyHandler.create()).handler(this::handleStudentInterest);
            router.post("/registration/modify-status").handler(this::handleRegistrationModifyStatus);
            router.post("/follow/request").handler(this::handleFollowRequestPost);
            router.post("/follow/request-modify").handler(this::handleFollowRequestModify);
        }
    }

    private void handleSocials(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));
        Optional<String> direction = Optional.of(routingContext.request().getParam("direction"));
        Optional<String> option = Optional.of(routingContext.request().getParam("option"));

        String username = routingContext.request().getParam("username");

        List<User> socials = null;

        if (username == null) { // from my page

            if (option.get().equals("followers")) {

                if (direction.get().equals("asc")) {
                    socials = userService.getFollowersPaginatedByUserIdOrderByCreateDateAsc(authentication.getId(), page.orElse(0), limit.orElse(5));
                } else {
                    socials = userService.getFollowersPaginatedByUserIdOrderByCreateDateDesc(authentication.getId(), page.orElse(0), limit.orElse(5));
                }
                JsonObject data = new JsonObject().put("socials", socials);
                routingContext.response().setStatusCode(200).end(data.encode());
            } else { // for fetching `followings`
                if (direction.get().equals("asc")) {
                    socials = userService.getFollowingsPaginatedByUserIdOrderByCreateDateAsc(authentication.getId(), page.orElse(0), limit.orElse(5));
                } else {
                    socials = userService.getFollowingsPaginatedByUserIdOrderByCreateDateDesc(authentication.getId(), page.orElse(0), limit.orElse(5));
                }
                JsonObject data = new JsonObject().put("socials", socials);
                routingContext.response().setStatusCode(200).end(data.encode());
            }
        } else { // from student-details
            User user = userService.getUserByUsername(username);

            if (option.get().equals("followers")) {

                if (direction.get().equals("asc")) {
                    socials = userService.getFollowersPaginatedByUserIdOrderByCreateDateAsc(user.getId(), page.orElse(0), limit.orElse(5));
                } else {
                    socials = userService.getFollowersPaginatedByUserIdOrderByCreateDateDesc(user.getId(), page.orElse(0), limit.orElse(5));
                }
                JsonObject data = new JsonObject().put("socials", socials);
                routingContext.response().setStatusCode(200).end(data.encode());
            } else { // for fetching `followings`
                if (direction.get().equals("asc")) {
                    socials = userService.getFollowingsPaginatedByUserIdOrderByCreateDateAsc(user.getId(), page.orElse(0), limit.orElse(5));
                } else {
                    socials = userService.getFollowingsPaginatedByUserIdOrderByCreateDateDesc(user.getId(), page.orElse(0), limit.orElse(5));
                }
                JsonObject data = new JsonObject().put("socials", socials);
                routingContext.response().setStatusCode(200).end(data.encode());
            }
        }
    }

    private void handleFollowingsCount(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");
        String username = routingContext.request().getParam("username");

        long followingsCount = 0;

        if (username == null) { // from my page
            followingsCount = userService.getFollowingsCountByUserId(authentication.getId());
        } else { // from student-details
            followingsCount = userService.getFollowingsCountByUserId(userService.getUserByUsername(username).getId());
        }

        JsonObject data = new JsonObject().put("count", followingsCount);
        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleFollowersCount(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");
        String username = routingContext.request().getParam("username");

        long followersCount = 0;

        if (username == null) { // from my page
            followersCount = userService.getFollowersCountByUserId(authentication.getId());
        } else { // from student-details
            followersCount = userService.getFollowersCountByUserId(userService.getUserByUsername(username).getId());
        }

        JsonObject data = new JsonObject().put("count", followersCount);
        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleFollowRequestModify(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");
        Long requestId = Long.parseLong(routingContext.request().getParam("requestId"));

        JsonObject data = new JsonObject().put("status", userService.updateRequestStatus(requestId, authentication));

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleFollowRequestGet(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        }

        String option = routingContext.request().getParam("option");
        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<UserProjection> users = userService.getRequestsPaginatedByRecipientIdAndByStatus(authentication.getId(), page.orElse(0), limit.orElse(7), option);

        JsonObject data = new JsonObject().put("users", users);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    // TODO: User can't add her/himself
    private void handleFollowRequestPost(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        }

        String recipientUsername = routingContext.request().getParam("recipientUsername");
        try {
            if (recipientUsername == null) { // from course-details
                userService.registerFollowRequest(Long.parseLong(routingContext.request().getParam("recipientId")), authentication.getUsername());
                routingContext.response().setStatusCode(200).setStatusMessage("Successfully done!").end();
            } else {
                userService.registerFollowRequest( // from student-details
                        userService.getUserByUsername(recipientUsername).getId(), authentication.getUsername());
                routingContext.response().setStatusCode(200).setStatusMessage("Successfully done!").end();
            }
        } catch (Exception e) {
            routingContext.response().setStatusCode(500).setStatusMessage("Already requested.").end();
        }

    }

    private void handleRegistrationModifyStatus(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            logger.info("[ Authentication entry point ]");
            routingContext.response().setStatusCode(401).end();
            return;
        }

        Long registrationId = Long.parseLong(routingContext.request().getParam("registrationId"));

        String modifiedStatus = courseService.modifyRegistrationStatus(registrationId);

        routingContext.response().setStatusCode(200).end(modifiedStatus);
    }

    private void handleUserDetails(RoutingContext routingContext) {
        String username = routingContext.request().getParam("username");
        User user = userService.getUserByUsername(username);

        if (user.getType().name().equals("TEACHER")) {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "teacher-details.html");
        } else if (user.getType().name().equals("STUDENT")) {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "student-details.html");
        }
    }

    private void handleTeacherCourse(RoutingContext routingContext) {

        // for pagination rendering
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }

        String username = routingContext.request().getParam("username");

        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<Course> courses = courseService.getPaginatedCoursesByPublishedDateAndByUsernameDescending(username, page.orElse(0), limit.orElse(3));

        JsonObject data = new JsonObject().put("courses", courses);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleStudentReview(RoutingContext routingContext) {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }
        String username = routingContext.request().getParam("username");

        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<Review> courses = courseService.getPaginatedReviewsByStudentUsername(username, page.orElse(0), limit.orElse(5));

        JsonObject data = new JsonObject().put("reviews", courses);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleStudentWishedCourse(RoutingContext routingContext) {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }

        String username = routingContext.request().getParam("username");

        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        List<CourseProjection> courses = courseService.getPaginatedCoursesByWisherUsername(username, page.orElse(0), limit.orElse(5));

        JsonObject data = new JsonObject().put("courses", courses);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleStudentEnrolledCourse(RoutingContext routingContext) {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }

        String username = routingContext.request().getParam("username");

        Optional<Integer> page = Optional.of(Integer.parseInt(routingContext.request().getParam("page")));
        Optional<Integer> limit = Optional.of(Integer.parseInt(routingContext.request().getParam("limit")));

        Optional<String> direction = Optional.of(routingContext.request().getParam("direction"));

        List<RegistrationProjection> courses = null;

        if (direction.get().equals("asc")) {
            courses = courseService.getPaginatedCoursesByUsernameAsc(username, page.orElse(0), limit.orElse(5));
        } else {
            courses = courseService.getPaginatedCoursesByUsernameDesc(username, page.orElse(0), limit.orElse(5));
        }

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

        routingContext.response().setStatusCode(200).setStatusMessage("Successfully registered :) Reload to see!").end();
    }

    private void handleStudentInterest(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            routingContext.response().setStatusCode(401).end();
            return;
        } else if (!authentication.getType().toString().equals("STUDENT")) {
            routingContext.response().setStatusCode(403).end();
            return;
        }

        JsonObject interestCommand = routingContext.getBodyAsJson();
        String interest = interestCommand.getString("name");

        userService.registerInterest(authentication.getUsername(), interest);

        routingContext.response().setStatusCode(200).setStatusMessage("Successfully registered :) Reload to see!").end();

    }

    private void handleTeacher(RoutingContext routingContext) {

        String username = routingContext.request().getParam("username");
        User user = userService.getUserByUsername(username);

        JsonObject data = new JsonObject();
        data.put("user", user);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleStudent(RoutingContext routingContext) {

        String username = routingContext.request().getParam("username");
        User user = userService.getUserByUsername(username);

        JsonObject data = new JsonObject();
        data.put("user", user);

        routingContext.response().setStatusCode(200).end(data.encode());
    }

    private void handleMyPage(RoutingContext routingContext) {

        UserProjection authentication = routingContext.session().get("Authentication");

        if (authentication == null) {
            routingContext.response().setStatusCode(401).end();
            return;
        }

        if (authentication.getType().toString().equals("TEACHER")) {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "teacher-mypage.html");
        } else if (authentication.getType().toString().equals("STUDENT")) {
            routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "student-mypage.html");
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
