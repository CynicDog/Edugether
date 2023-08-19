package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import org.jboss.logging.Logger;

import static org.example.util.constant.Location.HTML_ROOT;

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
                    .sendFile(HTML_ROOT + "home.html");
        });
    }

    public String[] publicUrls = {
            "/home"
    };
}
