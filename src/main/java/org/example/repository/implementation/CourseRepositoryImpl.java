package org.example.repository.implementation;

import org.example.entity.academics.Course;
import org.example.repository.CourseRepository;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;

public class CourseRepositoryImpl implements CourseRepository {

    private final Logger logger = Logger.getLogger(CourseRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public CourseRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void insertCourse(Course course) {

    }
}
