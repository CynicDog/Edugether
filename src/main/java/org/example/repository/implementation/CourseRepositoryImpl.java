package org.example.repository.implementation;

import org.example.entity.academics.Course;
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
    public List<Course> getPaginatedCoursesByPublishedDateAndByUsernameDescending(Integer page, Integer limit, String username) {
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
    public List<CourseProjection> getPaginatedCoursesByPublishedDateAscending(Integer page, Integer limit) {
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
                            "teacher.username) from Course c " +
                            "order by c.publishedDate asc", CourseProjection.class);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<CourseProjection> getPaginatedCoursesByPublishedDateDescending(Integer page, Integer limit) {
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
                            "teacher.username) from Course c " +
                            "order by c.publishedDate desc", CourseProjection.class);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public boolean insertCourse(Course course) {
        try {
            JpaOperationUtil.consume(entityManagerFactory, em -> {
                em.persist(course);
            });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
