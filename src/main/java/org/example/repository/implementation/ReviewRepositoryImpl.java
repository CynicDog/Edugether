package org.example.repository.implementation;

import org.example.entity.contents.Review;
import org.example.repository.ReviewRepository;
import org.example.util.JpaOperationUtil;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;

public class ReviewRepositoryImpl implements ReviewRepository {

    private final Logger logger = Logger.getLogger(CourseRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public ReviewRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void insertReview(Review review) {
        try {
            JpaOperationUtil.consume(entityManagerFactory, em -> {
                em.persist(review);
            });
        } catch (Exception e) {
            logger.info("[ ReviewRepositoryImpl.insertReview(Review review) ]: " + e.getMessage());
            throw e;
        }
    }
}
