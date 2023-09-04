package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.example.controller.CourseController;
import org.example.controller.HomeController;
import org.example.controller.UserController;
import org.example.repository.*;
import org.example.repository.implementation.*;
import org.example.service.CourseService;
import org.example.service.UserService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EdugetherMainApp extends AbstractVerticle {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("edugether");

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        UserRepository userRepository = new UserRepositoryImpl(emf);
        CourseRepository courseRepository = new CourseRepositoryImpl(emf);
        ReviewRepository reviewRepository = new ReviewRepositoryImpl(emf);
        FollowRequestRepository followRequestRepository = new FollowRequestRepositoryImpl(emf);
        FollowRepository followRepository = new FollowRepositoryImpl(emf);

        UserService userService = new UserService(userRepository, followRequestRepository, followRepository);
        CourseService courseService = new CourseService(courseRepository, userRepository, reviewRepository);

        UserController userController = new UserController(userService, courseService);
        userController.registerRoutes(vertx, router);

        HomeController homeController = new HomeController(userService);
        homeController.registerRoutes(vertx, router);

        CourseController courseController = new CourseController(userService, courseService);
        courseController.registerRoutes(vertx, router);

        server.requestHandler(router).listen(8080, result -> {
            if (result.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8080");
            } else {
                startPromise.fail(result.cause());
            }
        });
    }

    public static void main( String[] args ) {

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new EdugetherMainApp());
    }
}
