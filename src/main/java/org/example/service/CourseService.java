package org.example.service;

import org.example.repository.CourseRepository;
import org.jboss.logging.Logger;

public class CourseService {
    private final Logger logger = Logger.getLogger(CourseService.class);
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
}
