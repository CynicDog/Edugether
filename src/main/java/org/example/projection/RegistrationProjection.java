package org.example.projection;

import org.example.entity.academics.Course;
import org.example.util.enums.REGISTRATION_STATUS;

import java.util.Date;

public class RegistrationProjection {

    private Long id;
    private Course course;
    private REGISTRATION_STATUS registrationStatus = REGISTRATION_STATUS.ENROLLED;
    private Date enrolledDate;

    public RegistrationProjection(Long id, Course course, REGISTRATION_STATUS registrationStatus, Date enrolledDate) {
        this.id = id;
        this.course = course;
        this.registrationStatus = registrationStatus;
        this.enrolledDate = enrolledDate;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public REGISTRATION_STATUS getRegistrationStatus() {
        return registrationStatus;
    }

    public Date getEnrolledDate() {
        return enrolledDate;
    }
}
