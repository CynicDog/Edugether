package org.example.projection;

import org.example.util.enums.COURSE_STATUS;
import org.example.util.enums.SUBJECT_TITLE;

import java.util.Date;

public class CourseProjection {

    private Long id;
    private String name;
    private String description;
    private Date startingDay;
    private Date endingDay;
    private SUBJECT_TITLE subject;
    private COURSE_STATUS status;

    private String teacherUsername;

    public CourseProjection(Long id, String name, String description, Date startingDay, Date endingDay, SUBJECT_TITLE subject, String teacherUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startingDay = startingDay;
        this.endingDay = endingDay;
        this.subject = subject;
        this.teacherUsername = teacherUsername;
    }

    public CourseProjection(Long id, String name, String description, Date startingDay, Date endingDay, SUBJECT_TITLE subject, COURSE_STATUS status, String teacherUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startingDay = startingDay;
        this.endingDay = endingDay;
        this.subject = subject;
        this.status = status;
        this.teacherUsername = teacherUsername;
    }

    // for SqlResultSetMapping
    public CourseProjection(Long id, String name, String description, Date startingDay, Date endingDay, String subject, String status, String teacherUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startingDay = startingDay;
        this.endingDay = endingDay;
        this.subject = SUBJECT_TITLE.valueOf(subject);
        this.status = COURSE_STATUS.valueOf(status);
        this.teacherUsername = teacherUsername;
    }

    // for SqlResultSetMapping
    public CourseProjection(Long id, String name, Date startingDay, Date endingDay, String subject, String teacherUsername) {
        this.id = id;
        this.name = name;
        this.startingDay = startingDay;
        this.endingDay = endingDay;
        this.subject = SUBJECT_TITLE.valueOf(subject);
        this.teacherUsername = teacherUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartingDay() {
        return startingDay;
    }

    public void setStartingDay(Date startingDay) {
        this.startingDay = startingDay;
    }

    public Date getEndingDay() {
        return endingDay;
    }

    public void setEndingDay(Date endingDay) {
        this.endingDay = endingDay;
    }

    public SUBJECT_TITLE getSubject() {
        return subject;
    }

    public void setSubject(SUBJECT_TITLE subject) {
        this.subject = subject;
    }

    public COURSE_STATUS getStatus() {
        return status;
    }

    public void setStatus(COURSE_STATUS status) {
        this.status = status;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }
}
