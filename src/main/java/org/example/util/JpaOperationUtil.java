package org.example.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Function;

public class JpaOperationUtil {

    public static void consume(EntityManagerFactory emf, Consumer<EntityManager> operation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            operation.accept(em);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
    }

    public static <T> T apply(EntityManagerFactory emf, Function<EntityManager, T> operation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            T result = operation.apply(em);
            transaction.commit();

            return result;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        } finally {
            em.close();
        }
    }
}
