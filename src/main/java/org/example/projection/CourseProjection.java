package org.example.projection;

import org.example.entity.users.Student;
import org.example.util.enums.COURSE_STATUS;
import org.example.util.enums.SUBJECT_TITLE;

import java.util.Date;
import java.util.Set;

public class CourseProjection {

    private Long id;
    private String name;
    private String description;
    private Date startingDay;
    private Date endingDay;
    private SUBJECT_TITLE subject;
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
    public Long getId() {
        return id;
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

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }
}
