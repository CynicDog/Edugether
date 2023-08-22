package org.example;

import org.example.entity.socials.Society;
import org.example.entity.socials.SocietyEvent;
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

    @Test
    public void studentSocietyEventCardinality() {

        Student student1 = new Student("miles", "1234", "miles@test.com", TYPE.STUDENT);
        student1.setInterests(Arrays.asList("Mathematics", "Philosophy", "Physics"));
        em.persist(student1);

        Student student2 = new Student("oscar", "1234", "oscar@test.com", TYPE.STUDENT);
        student2.setInterests(Arrays.asList("Computer Science", "Music", "Art"));
        em.persist(student2);

        Society society = new Society("Math and Science Enthusiasts");
        society.getMembers().add(student1);
        society.getMembers().add(student2);
        em.persist(society);

        SocietyEvent event1 = new SocietyEvent("Park Avenue Meetup");
        event1.setSociety(society);
        event1.getAttendants().addAll(Arrays.asList(student1, student2));
        em.persist(event1);

        SocietyEvent event2 = new SocietyEvent("Tech Workshop at Washington Square Park");
        event2.setSociety(society);
        event2.getAttendants().add(student1);
        em.persist(event2);

        SocietyEvent event3 = new SocietyEvent("Discussion at Local Cafe");
        event3.setSociety(society);
        event3.getAttendants().add(student2);
        em.persist(event3);

        em.getTransaction().commit();
    }
}