package org.example.entity.academics;

import org.example.entity.users.Teacher;
import org.example.util.enums.COURSE_STATUS;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quota;

    private Integer registrationCount;

    @Enumerated(EnumType.STRING)
    private COURSE_STATUS courseStatus;

    @CreationTimestamp
    private Date publishedDate;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private Teacher teacher;

    public Course() {
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

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getRegistrationCount() {
        return registrationCount;
    }

    public void setRegistrationCount(Integer registrationCount) {
        this.registrationCount = registrationCount;
    }

    public COURSE_STATUS getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(COURSE_STATUS courseStatus) {
        this.courseStatus = courseStatus;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
