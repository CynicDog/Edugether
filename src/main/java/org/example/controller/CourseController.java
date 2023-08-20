package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.service.CourseService;
import org.example.service.UserService;
import org.jboss.logging.Logger;

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
    }
}
