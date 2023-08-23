package org.example.repository;

import org.example.entity.academics.Course;
import org.example.projection.CourseProjection;

import java.util.List;

public interface CourseRepository {

    boolean insertCourse(Course course);

    List<CourseProjection> getPaginatedCoursesByPublishedDateDescending(Integer page, Integer limit);

    List<Course> getPaginatedCoursesByPublishedDateAndByUsernameDescending(Integer page, Integer limit, String username);

    List<CourseProjection> getPaginatedCoursesByPublishedDateAscending(Integer page, Integer limit);
}

