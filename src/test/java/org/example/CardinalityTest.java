package org.example;

import org.example.entity.users.Student;
import org.example.util.enums.TYPE;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;

public class CardinalityTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    public void setup() {
        emf = Persistence.createEntityManagerFactory("edugether_test");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        if (em != null) {
            em.close();
        }

        if (emf != null) {
            emf.close();
        }

        em = null;
        emf = null;
    }
}