package org.example.service;

import io.vertx.core.json.JsonObject;
import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.contents.Review;
import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.projection.CourseProjection;
import org.example.repository.CourseRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.UserRepository;
import org.example.util.enums.REVIEW_SENTIMENT;
import org.jboss.logging.Logger;

import java.text.SimpleDateFormat;
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

    public List<CourseProjection> getPaginatedCoursesByPublishedDateDescending(Integer page, Integer limit) {

        return courseRepository.getPaginatedCoursesByPublishedDateDescending(page, limit);
    }

    public List<Course> getPaginatedCoursesByPublishedDateAndByUsernameDescending(Integer page, Integer limit, String username) {

        return courseRepository.getPaginatedCoursesByPublishedDateAndByUsernameDescending(page, limit, username);
    }

    public List<CourseProjection> getPaginatedCoursesByPublishedDateAscending(Integer page, Integer limit) {

        return courseRepository.getPaginatedCoursesByPublishedDateAscending(page, limit);
    }

    public void enrollOnCourse(String username, Long courseId) {

        Student student = userRepository.loadStudentByUsername(username);
        Course course = courseRepository.getCourseById(courseId);

        Registration registration = new Registration(student, course);

        try {
            courseRepository.insertRegistration(registration);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void registerReview(JsonObject reviewCommand) {

        Student student = userRepository.loadStudentByUsername(reviewCommand.getString("student"));
        Course course = courseRepository.getCourseById(Long.valueOf(reviewCommand.getInteger("courseId")));
        REVIEW_SENTIMENT sentiment = REVIEW_SENTIMENT.valueOf(reviewCommand.getString("sentiment").toUpperCase());
        String content = reviewCommand.getString("content");

        Review review = new Review(student, course, sentiment, content);

        try {
            reviewRepository.insertReview(review);
        } catch (Exception e) {
            throw e;
        }
    }
}
