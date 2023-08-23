package org.example.service;

import io.vertx.core.json.JsonObject;
import org.example.entity.academics.Course;
import org.example.entity.academics.Registration;
import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.projection.CourseProjection;
import org.example.repository.CourseRepository;
import org.example.repository.UserRepository;
import org.jboss.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CourseService {
    private final Logger logger = Logger.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public boolean registerCourse(JsonObject courseCommand) {

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
            return courseRepository.insertCourse(course);

        } catch (ParseException e) {
            e.printStackTrace();
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

    public boolean enrollOnCourse(String username, Long courseId) {

        Student student = userRepository.loadStudentByUsername(username);
        Course course = courseRepository.getCourseById(courseId);

        Registration registration = new Registration(student, course);

        if (courseRepository.insertRegistration(registration)) {
            return true;
        }
        return false;
    }
}
