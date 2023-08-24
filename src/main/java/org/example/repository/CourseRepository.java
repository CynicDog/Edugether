package org.example.repository;

import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.users.Student;
import org.example.projection.CourseProjection;

import java.util.List;

public interface CourseRepository {
    Course getCourseById(Long courseId);
    void insertCourse(Course course);
    List<CourseProjection> getPaginatedCoursesByPublishedDateDescending(Integer page, Integer limit);
    List<Course> getPaginatedCoursesByPublishedDateAndByUsernameDescending(String username, Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByPublishedDateAscending(Integer page, Integer limit);
    void insertRegistration(Registration registration);
    boolean isRegistered(Long studentId, Long courseId);
    List<Student> getStudentsByRegistration_CourseId(Long courseId);
}

