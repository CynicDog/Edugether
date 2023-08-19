package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.example.controller.HomeController;
import org.example.controller.UserController;
import org.example.repository.UserRepository;
import org.example.repository.implementation.UserRepositoryImpl;
import org.example.service.UserService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App extends AbstractVerticle {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("edugether");

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        UserRepository userRepository = new UserRepositoryImpl(emf);
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        userController.registerRoutes(vertx, router);

        HomeController homeController = new HomeController();
        homeController.registerRoutes(vertx, router);

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
        vertx.deployVerticle(new App());
    }
}
