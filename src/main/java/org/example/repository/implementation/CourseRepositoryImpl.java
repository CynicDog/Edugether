package org.example.repository.implementation;

import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.academics.Review;
import org.example.entity.users.Student;
import org.example.projection.CourseProjection;
import org.example.projection.RegistrationProjection;
import org.example.repository.CourseRepository;
import org.example.util.JpaOperationUtil;
import org.example.util.enums.REVIEW_SENTIMENT;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class CourseRepositoryImpl implements CourseRepository {

    private final Logger logger = Logger.getLogger(CourseRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public CourseRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void updateRegistration(Registration registration) {
        JpaOperationUtil.consume(entityManagerFactory, em -> {
            em.merge(registration);
        });
    }

    @Override
    public Registration getRegistrationByRegistrationId(Long registrationId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            return em.find(Registration.class, registrationId);
        });
    }

    @Override
    public boolean isRegistered(Long studentId, Long courseId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            TypedQuery<Long> query = em.createQuery("select count(r) from Registration r where r.student.id = :studentId and r.course.id = :courseId", Long.class);

            query.setParameter("studentId", studentId);
            query.setParameter("courseId", courseId);

            return query.getSingleResult() > 0;
        });
    }

    @Override
    public void insertRegistration(Registration registration) {
        try {
            JpaOperationUtil.consume(entityManagerFactory, em -> {
                em.persist(registration);
            });
        } catch (Exception ex) {
            logger.info("[ CourseRepositoryImpl.insertRegistration(Registration registration) ]: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByReviewSentimentCriticized(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery(
                    "select new org.example.projection.CourseProjection(" +
                            "c.id, " +
                            "c.name, " +
                            "c.description, " +
                            "c.startingDay, " +
                            "c.endingDay, " +
                            "c.subject, " +
                            "c.teacher.username) " +
                            "from Course c " +
                            "left join Review r on r.course.id = c.id " +
                            "where r.reviewSentiment in :sentiment " +
                            "group by c.id, c.name, c.description, c.startingDay, c.endingDay, c.subject, c.teacher.username " +
                            "order by count(r.id) desc", CourseProjection.class);

            Set<REVIEW_SENTIMENT> acclaimedSentiments = EnumSet.of(REVIEW_SENTIMENT.DISLIKED, REVIEW_SENTIMENT.CONFUSED);

            query.setParameter("sentiment", acclaimedSentiments);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByReviewSentimentMixed(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery(
                    "select new org.example.projection.CourseProjection(" +
                            "c.id, " +
                            "c.name, " +
                            "c.description, " +
                            "c.startingDay, " +
                            "c.endingDay, " +
                            "c.subject, " +
                            "c.teacher.username) " +
                            "from Course c " +
                            "left join Review r on r.course.id = c.id " +
                            "where r.reviewSentiment in :sentiment " +
                            "group by c.id, c.name, c.description, c.startingDay, c.endingDay, c.subject, c.teacher.username " +
                            "order by count(r.id) desc", CourseProjection.class);

            Set<REVIEW_SENTIMENT> acclaimedSentiments = EnumSet.of(REVIEW_SENTIMENT.MIXED, REVIEW_SENTIMENT.NEUTRAL);

            query.setParameter("sentiment", acclaimedSentiments);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByReviewSentimentAcclaimed(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery(
                    "select new org.example.projection.CourseProjection(" +
                            "c.id, " +
                            "c.name, " +
                            "c.description, " +
                            "c.startingDay, " +
                            "c.endingDay, " +
                            "c.subject, " +
                            "c.teacher.username) " +
                            "from Course c " +
                            "left join Review r on r.course.id = c.id " +
                            "where r.reviewSentiment in :sentiment " +
                            "group by c.id, c.name, c.description, c.startingDay, c.endingDay, c.subject, c.teacher.username " +
                            "order by count(r.id) desc", CourseProjection.class);

            Set<REVIEW_SENTIMENT> acclaimedSentiments = EnumSet.of(
                    REVIEW_SENTIMENT.LOVED,
                    REVIEW_SENTIMENT.IMPRESSED,
                    REVIEW_SENTIMENT.INSPIRED,
                    REVIEW_SENTIMENT.OVERWHELMED);

            query.setParameter("sentiment", acclaimedSentiments);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByRegistrationCount(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery(
                    "select new org.example.projection.CourseProjection(" +
                            "c.id, " +
                            "c.name, " +
                            "c.description, " +
                            "c.startingDay, " +
                            "c.endingDay, " +
                            "c.subject, " +
                            "c.teacher.username) " +
                            "from Course c " +
                            "left join Registration r on r.course.id = c.id " +
                            "group by c.id, c.name, c.description, c.startingDay, c.endingDay, c.subject, c.teacher.username " +
                            "order by count(r.id) desc", CourseProjection.class);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByWisherId(Long studentId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            Query query = em.createNativeQuery("select " +
                    "c.id, " +
                    "c.name, " +
                    "c.startingDay, " +
                    "c.endingDay, " +
                    "c.subject, " +
                    "u.username as teacherUsername " +
                    "from Course c " +
                    "inner join Teacher t on C.teacherId = t.id " +
                    "inner join User u on t.id = u.id " +
                    "inner join WishListCourses wlc on c.id = wlc.courseId " +
                    "where wlc.studentId = :studentId ", "CourseProjectionMapping");

            query.setParameter("studentId", studentId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<RegistrationProjection> getPaginatedCoursesByUserIdOrderByEnrolledDateAsc(Long studentId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;
            TypedQuery<RegistrationProjection> query = em.createQuery(
                    "select new org.example.projection.RegistrationProjection(" +
                            "r.id, " +
                            "r.course, " +
                            "r.registrationStatus, " +
                            "r.enrolledDate) " +
                            "from Registration r where r.student.id = :studentId order by r.enrolledDate asc ",
                    RegistrationProjection.class);

            query.setParameter("studentId", studentId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<RegistrationProjection> getPaginatedCoursesByUserIdOrderByEnrolledDateDesc(Long studentId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;
            TypedQuery<RegistrationProjection> query = em.createQuery(
                    "select new org.example.projection.RegistrationProjection(" +
                            "r.id, " +
                            "r.course, " +
                            "r.registrationStatus, " +
                            "r.enrolledDate) " +
                            "from Registration r where r.student.id = :studentId order by r.enrolledDate desc",
                    RegistrationProjection.class);

            query.setParameter("studentId", studentId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<Course> getPaginatedCoursesByUsernameOrderByPublishedDateDesc(String username, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;
            TypedQuery<Course> query = em.createQuery(
                    "select distinct c from Course c left join fetch c.wishers where c.teacher.username = :username order by c.publishedDate desc", Course.class);

            query.setParameter("username", username);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByPublishedDateAsc(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery("select new org.example.projection.CourseProjection(" +
                    "c.id, " +
                    "c.name, " +
                    "c.description, " +
                    "c.startingDay, " +
                    "c.endingDay, " +
                    "c.subject, " +
                    "c.teacher.username) from Course c " +
                    "order by c.publishedDate asc", CourseProjection.class);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByPublishedDateDesc(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery("select new org.example.projection.CourseProjection(" +
                    "c.id, " +
                    "c.name, " +
                    "c.description, " +
                    "c.startingDay, " +
                    "c.endingDay, " +
                    "c.subject, " +
                    "c.teacher.username) from Course c " + "order by c.publishedDate desc", CourseProjection.class);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public void insertCourse(Course course) {
        try {
            JpaOperationUtil.consume(entityManagerFactory, em -> {
                em.persist(course);
            });
        } catch (Exception e) {
            logger.info("[ CourseRepositoryImpl.insertCourse(Course course) ]: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Course getCourseById(Long courseId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            return em.find(Course.class, courseId);
        });
    }

    @Override
    public List<Student> getStudentsByRegistration_CourseId(Long courseId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            return em.createQuery(
                    "select r.student from Registration r where r.course.id = :courseId and r.registrationStatus = 'ENROLLED'", Student.class).setParameter("courseId", courseId).getResultList();
        });
    }

    @Override
    public void updateReview(Review review) {
        try {
            JpaOperationUtil.consume(entityManagerFactory, em -> {
                em.merge(review);
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void updateCourse(Course course) {
        try {
            JpaOperationUtil.consume(entityManagerFactory, em -> {
                em.merge(course);
            });
        } catch (Exception e) {
            throw e;
        }
    }
}
