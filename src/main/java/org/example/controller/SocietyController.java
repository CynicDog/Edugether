package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.service.SocietyService;
import org.example.service.UserService;
import org.jboss.logging.Logger;

import static org.example.util.constant.PageLocation.PUBLIC;

public class SocietyController implements Controller {
    private final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;
    private final SocietyService societyService;

    public SocietyController(UserService userService, SocietyService societyService) {
        this.userService = userService;
        this.societyService = societyService;
    }

    @Override
    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        {   // GET handlers
            router.get("/society").handler(routingContext -> { routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "society-entry.html"); });
        }
    }
}
