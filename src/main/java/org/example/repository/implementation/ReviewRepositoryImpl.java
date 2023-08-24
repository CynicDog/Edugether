package org.example.repository.implementation;

import org.example.entity.contents.Review;
import org.example.projection.ReviewProjection;
import org.example.repository.ReviewRepository;
import org.example.util.JpaOperationUtil;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepository {

    private final Logger logger = Logger.getLogger(CourseRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public ReviewRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<ReviewProjection> getPaginatedReviewsByCourseIdAndCreateDateDescending(Long courseId,
                                                                                       Integer page,
                                                                                       Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;
            TypedQuery<ReviewProjection> query = em.createQuery("select new org.example.projection.ReviewProjection(" +
                    "r.id, " +
                    "r.user, " +
                    "r.reviewSentiment, " +
                    "r.createDate, " +
                    "r.content, " +
                    "r.likedCount, " +
                    "(select count(r2) from Review r2 where r2.course.id = :courseId) " +
                    ") from Review r where r.course.id = :courseId order by r.createDate desc", ReviewProjection.class);

            query.setParameter("courseId", courseId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
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
