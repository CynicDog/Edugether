package org.example;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ExtendWith(VertxExtension.class)
public class HttpClientTest {

    private final EntityManagerFactory entityManagerFactory;

    public HttpClientTest(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("edugether_test");
    }

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {

        vertx.deployVerticle(new EdugetherMainApp())
                .onSuccess(success -> testContext.completeNow())
                .onFailure(failure -> testContext.failNow(failure));
    }

//    @Test
//    public void  ...
}
