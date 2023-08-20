package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import org.jboss.logging.Logger;

import static org.example.util.constant.PageLocation.PUBLIC;

public class HomeController implements Controller{
    private final Logger logger = Logger.getLogger(UserController.class);

    public HomeController() {
    }

    @Override
    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());

        // http GET http://localhost:8080/home
        router.get("/home").handler(routingContext -> {
            routingContext.response()
                    .putHeader("Content-Type", "text/html")
                    .sendFile(PUBLIC + "home.html");
        });
    }

    public String[] publicUrls = {
            "/home"
    };
}
