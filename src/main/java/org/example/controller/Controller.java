package org.example.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface Controller {

    void registerRoutes(Vertx vertx, Router router);
}
