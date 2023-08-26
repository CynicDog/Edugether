package org.example.repository;

import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.academics.Review;
import org.example.entity.users.Student;
import org.example.projection.CourseProjection;
import org.example.projection.RegistrationProjection;

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
    void updateReview(Review review);
    void updateCourse(Course course);
    List<RegistrationProjection> getPaginatedCoursesByEnrolledDateByUsernameDescending(Long studentId, Integer page, Integer limit);
    List<RegistrationProjection> getPaginatedCoursesByEnrolledDateByUsernameAscending(Long studentId, Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByWisherUsernameDescending(Long studentId, Integer page, Integer limit);
    Registration getRegistrationByRegistrationId(Long registrationId);
    void updateRegistration(Registration registration);
}

