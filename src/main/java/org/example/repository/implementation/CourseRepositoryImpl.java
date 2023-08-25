package org.example.repository.implementation;

import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.academics.Review;
import org.example.entity.users.Student;
import org.example.projection.CourseProjection;
import org.example.repository.CourseRepository;
import org.example.util.JpaOperationUtil;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {

    private final Logger logger = Logger.getLogger(CourseRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public CourseRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
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
    public List<Course> getPaginatedCoursesByPublishedDateAndByUsernameDescending(String username, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;
            TypedQuery<Course> query = em.createQuery("select distinct c from Course c left join fetch c.wishers where c.teacher.username = :username order by c.publishedDate desc", Course.class);

            query.setParameter("username", username);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByPublishedDateAscending(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery("select new org.example.projection.CourseProjection(" + "c.id, " + "c.name, " + "c.description, " + "c.startingDay, " + "c.endingDay, " + "c.subject, " + "teacher.username) from Course c " + "order by c.publishedDate asc", CourseProjection.class);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByPublishedDateDescending(Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<CourseProjection> query = em.createQuery("select new org.example.projection.CourseProjection(" + "c.id, " + "c.name, " + "c.description, " + "c.startingDay, " + "c.endingDay, " + "c.subject, " + "teacher.username) from Course c " + "order by c.publishedDate desc", CourseProjection.class);

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

            return em.createQuery("select r.student from Registration r where r.course.id = :courseId", Student.class).setParameter("courseId", courseId).getResultList();
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
}
