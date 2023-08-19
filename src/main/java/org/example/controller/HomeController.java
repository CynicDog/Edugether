package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.example.service.UserService;
import org.jboss.logging.Logger;

public class HomeController {

    private final Logger logger = Logger.getLogger(UserController.class);

    public HomeController() {
    }

    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());

        // http GET http://localhost:8080/home
        router.route(HttpMethod.GET, "/home").handler(routingContext -> {
            routingContext.response()
                    .putHeader("Content-Type", "text/html")
                    .sendFile("src/main/resources/public/home.html");
        });
    }
}
