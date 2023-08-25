package org.example.repository.implementation;

import org.example.entity.academics.Review;
import org.example.projection.ReviewProjection;
import org.example.repository.ReviewRepository;
import org.example.util.JpaOperationUtil;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepository {

    private final Logger logger = Logger.getLogger(CourseRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public ReviewRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public BigInteger getReviewLikedCount(Long reviewId) {
        return (BigInteger) JpaOperationUtil.apply(entityManagerFactory, em -> {

            try {
                return em.createNativeQuery("select count(*) from LikedReviews where reviewId = :reviewId")
                        .setParameter("reviewId", reviewId)
                        .getSingleResult();
            } catch (Exception e) {
                logger.info("[ ReviewRepositoryImpl.getReviewLikedCount(Long reviewId, Long userId) ]: " + e.getMessage());
                throw e;
            }
        });
    }



    @Override
    public List<ReviewProjection> getPaginatedReviewsByCourseIdAndCreateDateDescending(Long courseId,
                                                                                       Integer page,
                                                                                       Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;
            TypedQuery<ReviewProjection> query = em.createQuery("select new org.example.projection.ReviewProjection(" +
                    "r.id, " +
                    "r.writer, " +
                    "r.course.teacher.id, " +
                    "r.reviewSentiment, " +
                    "r.createDate, " +
                    "r.content, " +
                    "(select count(1) from r.likers), " +
                    "(select count(r2) from Review r2 where r2.course.id = :courseId) " +
                    ") from Review r where r.course.id = :courseId order by r.createDate desc", ReviewProjection.class);

            query.setParameter("courseId", courseId);
            query.setFirstResult(startIndex);
            query.setMaxResults(limit);
            List<ReviewProjection> founds = query.getResultList();

            founds.forEach(found -> {
                found.setLikedByTeacher((BigInteger) em.createNativeQuery(
                        "select count(*) from LikedReviews where reviewId = :reviewId and userId = :courseTeacherId")
                        .setParameter("reviewId", found.getId())
                        .setParameter("courseTeacherId", found.getCourseTeacherId())
                        .getSingleResult());
            });
            return founds;
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

    @Override
    public Review getReviewById(Long reviewId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            return em.find(Review.class, reviewId);
        });
    }
}
