package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.example.entity.users.User;
import org.example.projection.UserProjection;
import org.example.service.UserService;
import org.jboss.logging.Logger;

import java.security.Principal;

import static org.example.util.constant.PageLocation.PUBLIC;

public class HomeController implements Controller {
    private final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;
    public String[] publicUrls = {"/", "/home"};

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void registerRoutes(Vertx vertx, Router router) {

        router.route().handler(StaticHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        {   // GET handlers
            router.get("/home").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "home.html");
            });

            router.get("/").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "home.html");
            });

            router.get("/login").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "login.html");
            });

            router.get("/nav-authenticated").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "nav-authenticated.html");
            });

            router.get("/nav-anonymous").handler(routingContext -> {
                routingContext.response().putHeader("Content-Type", "text/html").sendFile(PUBLIC + "nav-anonymous.html");
            });
        }

        {   // POST handlers
            router.post("/logout").handler(BodyHandler.create()).handler(this::handleLogout);
            router.post("/login").handler(BodyHandler.create()).handler(this::handleLogin);
        }
    }

    private void handleLogout(RoutingContext routingContext) {

        String username = routingContext.getBodyAsJson().getString("username");
        Principal authenticatedPrincipal = routingContext.session().get("Authentication");

        if (authenticatedPrincipal.getName().equals(username)) {
            routingContext.session().remove("Authentication");
            routingContext.response().setStatusCode(200).end();
        } else {
            routingContext.response().setStatusCode(400).setStatusMessage("Can't find the principal").end();
        }
    }

    private void handleLogin(RoutingContext routingContext) {

        JsonObject credentials = routingContext.getBodyAsJson();

        User found = userService.authenticate(credentials);

        if (found != null) {
            Principal authenticatedPrincipal = new UserProjection(found.getId(), found.getUsername());
            routingContext.session().put("Authentication", authenticatedPrincipal);

            JsonObject userInfo = new JsonObject().put("username", authenticatedPrincipal.getName());

            routingContext.response().setStatusCode(200).putHeader("Content-Type", "application/json").end(userInfo.encode());
        } else {
            routingContext.response().setStatusCode(401).setStatusMessage("Bad credentials!").end();
        }
    }
}
