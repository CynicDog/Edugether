package org.example.repository;

import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.academics.Review;
import org.example.entity.users.Student;
import org.example.projection.CourseProjection;
import org.example.projection.RegistrationProjection;
import org.example.projection.ReviewProjection;

import java.util.List;

public interface CourseRepository {
    Course getCourseById(Long courseId);
    void insertCourse(Course course);
    List<CourseProjection> getPaginatedCoursesByPublishedDateDesc(Integer page, Integer limit);
    List<Course> getPaginatedCoursesByUsernameOrderByPublishedDateDesc(String username, Integer page, Integer limit);
    List<Course> getPaginatedCoursesByUsernameOrderByPublishedDateAsc(String username, Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByPublishedDateAsc(Integer page, Integer limit);
    void insertRegistration(Registration registration);
    boolean isRegistered(Long studentId, Long courseId);
    List<Student> getStudentsByRegistration_CourseId(Long courseId);
    void updateReview(Review review);
    void updateCourse(Course course);
    List<RegistrationProjection> getPaginatedCoursesByUserIdOrderByEnrolledDateDesc(Long studentId, Integer page, Integer limit);
    List<RegistrationProjection> getPaginatedCoursesByUserIdOrderByEnrolledDateAsc(Long studentId, Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByWisherId(Long studentId, Integer page, Integer limit);
    Registration getRegistrationByRegistrationId(Long registrationId);
    void updateRegistration(Registration registration);
    List<CourseProjection> getPaginatedCoursesByRegistrationCount(Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByReviewSentimentAcclaimed(Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByReviewSentimentMixed(Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByReviewSentimentCriticized(Integer page, Integer limit);
    List<CourseProjection> getPaginatedCoursesByReviewOpened(Integer page, Integer limit);
    CourseProjection getCourseByRegistrationCount(int limit);
    CourseProjection getCourseByWishCount(int limit);
    CourseProjection getCourseByReviewCount(int limit);
    List<CourseProjection> getCoursesOrderByPublishedDateLimit(int limit);
    Course getCourseByCourseName(String courseName);
    void deleteCourseByCourseName(String courseName);
}


