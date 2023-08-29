package org.example.service;

import io.vertx.core.json.JsonObject;
import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.academics.Review;
import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.entity.users.User;
import org.example.projection.CourseProjection;
import org.example.projection.RegistrationProjection;
import org.example.projection.ReviewProjection;
import org.example.repository.CourseRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.UserRepository;
import org.example.util.enums.FETCHING_OPTION;
import org.example.util.enums.REGISTRATION_STATUS;
import org.example.util.enums.REVIEW_SENTIMENT;
import org.jboss.logging.Logger;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class CourseService {
    private final Logger logger = Logger.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void registerCourse(JsonObject courseCommand) {

        Teacher teacher = userRepository.loadTeacherByUsername(courseCommand.getString("teacher"));

        try {
            Course course = new Course(
                    courseCommand.getString("name"),
                    courseCommand.getString("description"),
                    dateFormat.parse(courseCommand.getString("startingDay")),
                    dateFormat.parse(courseCommand.getString("closingDay")),
                    courseCommand.getString("subject").toUpperCase(),
                    teacher
            );
            courseRepository.insertCourse(course);

        } catch (Exception e) {
            logger.info("[ registerCourse ]: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<CourseProjection> getPaginatedCoursesByOption(String option, Integer page, Integer limit) {

        if (option.equals(FETCHING_OPTION.NEWEST.toString().toLowerCase())) {
            return courseRepository.getPaginatedCoursesByPublishedDateDescending(page, limit);
        } else if (option.equals(FETCHING_OPTION.OLDEST.toString().toLowerCase())) {
            return courseRepository.getPaginatedCoursesByPublishedDateAscending(page, limit);
        } else if (option.equals(FETCHING_OPTION.POPULAR.toString().toLowerCase())) {
            return courseRepository.getPaginatedCoursesByRegistrationCount(page, limit);
        } else if (option.equals(FETCHING_OPTION.ACCLAIMED.toString().toLowerCase())) {
            return courseRepository.getPaginatedCoursesByReviewSentimentAcclaimed(page, limit);
        } else if (option.equals(FETCHING_OPTION.MIXED.toString().toLowerCase())) {
            return courseRepository.getPaginatedCoursesByReviewSentimentMixed(page, limit);
        } else { // criticized
            return courseRepository.getPaginatedCoursesByReviewSentimentCriticized(page, limit);
        }
    }

    public void enrollOnCourse(String username, Long courseId) {

        Student student = userRepository.loadStudentByUsername(username);
        Course course = courseRepository.getCourseById(courseId);

        Registration registration = new Registration(student, course);

        try {
            courseRepository.insertRegistration(registration);
        } catch (Exception e) {
            logger.info("[ enrollOnCourse(String username, Long courseId) ]: " + e.getMessage());
            throw e;
        }
    }

    public void wishForCourse(String username, Long courseId) {

        Student student = userRepository.loadStudentByUsername(username);
        Course course = courseRepository.getCourseById(courseId);

        // TODO: `ManyToMany` unique constraints exception throwing fails ..... why?
        if (course.getWishers().stream().anyMatch(wisher -> wisher.getUsername().equals(username))) {
            logger.info("course.getWishers().contains(student)");
            throw new IllegalStateException("Already wished for.");
        }

        course.addWisher(student);

        try {
            courseRepository.updateCourse(course);
        } catch (Exception e) {
            logger.info("[ wishForCourse(String username, Long courseId) ]: " + e.getMessage());
            throw e;
        }
    }

    public void registerReview(JsonObject reviewCommand) {

        Long studentId = Long.parseLong(reviewCommand.getString("studentId"));
        Long courseId = Long.parseLong(reviewCommand.getString("courseId"));

        if (courseRepository.isRegistered(studentId, courseId)) {
            // is registered
            Student student = userRepository.getStudentById(studentId);
            Course course = courseRepository.getCourseById(courseId);
            REVIEW_SENTIMENT sentiment = REVIEW_SENTIMENT.valueOf(reviewCommand.getString("sentiment").toUpperCase());
            String content = reviewCommand.getString("content");

            Review review = new Review(student, course, sentiment, content);

            try {
                reviewRepository.insertReview(review);
            } catch (Exception e) { // PersistenceException (DataTooLongException, SQLDataException, etc.)
                throw e;
            }
        } else {
            // is not registered
            throw new IllegalStateException("Not yet registered for the course.");
        }
    }

    public Course getCourseById(Long courseId) {

        return courseRepository.getCourseById(courseId);
    }

    public List<Student> getStudentsByRegistration_CourseId(Long courseId) {

        return courseRepository.getStudentsByRegistration_CourseId(courseId);
    }

    public List<Course> getPaginatedCoursesByPublishedDateAndByUsernameDescending(String username, Integer page, Integer limit) {

        return courseRepository.getPaginatedCoursesByPublishedDateAndByUsernameDescending(username, page, limit);
    }

    public List<ReviewProjection> getPaginatedReviewsByCourseIdAndCreateDateDescending(Long courseId, Integer page, Integer limit) {

        return reviewRepository.getPaginatedReviewsByCourseIdAndCreateDateDescending(courseId, page, limit);
    }

    public List<RegistrationProjection> getPaginatedCoursesByEnrolledDateByUsernameDescending(String username, Integer page, Integer limit) {

        User user = userRepository.loadUserByUsername(username);

        return courseRepository.getPaginatedCoursesByEnrolledDateByUsernameDescending(user.getId(), page, limit);
    }

    public List<RegistrationProjection> getPaginatedCoursesByEnrolledDateByUsernameAscending(String username, Integer page, Integer limit) {

        User user = userRepository.loadUserByUsername(username);

        return courseRepository.getPaginatedCoursesByEnrolledDateByUsernameAscending(user.getId(), page, limit);
    }

    public List<CourseProjection> getPaginatedCoursesByWisherUsernameDescending(String username, Integer page, Integer limit) {

        User user = userRepository.loadUserByUsername(username);

        return courseRepository.getPaginatedCoursesByWisherUsernameDescending(user.getId(), page, limit);
    }

    public BigInteger registerLike(Long reviewId, Long userId) {

        User user = userRepository.getUserById(userId);
        Review review = reviewRepository.getReviewById(reviewId);

        // TODO: `ManyToMany` unique constraints exception throwing fails ..... why?
        if (review.getLikers().stream().anyMatch(liker -> liker.getUsername().equals(user.getUsername()))) {
            throw new IllegalStateException("Already liked.");
        }

        review.addLikers(user);

        try {
            courseRepository.updateReview(review);
        } catch (Exception e) {
            throw e;
        }

        try {
            BigInteger likedCount = reviewRepository.getReviewLikedCount(reviewId);
            return likedCount;
        } catch (Exception e) {
            throw e;
        }
    }

    public String modifyRegistrationStatus(Long registrationId) {

        Registration registration = courseRepository.getRegistrationByRegistrationId(registrationId);

        REGISTRATION_STATUS modified = (registration.getRegistrationStatus() == REGISTRATION_STATUS.ENROLLED) ?
                REGISTRATION_STATUS.ABORTED :
                REGISTRATION_STATUS.ENROLLED;

        registration.setRegistrationStatus(modified);
        courseRepository.updateRegistration(registration);

        return modified.toString();
    }
}
