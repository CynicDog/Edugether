package org.example.entity.academics;

import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.example.util.enums.COURSE_STATUS;
import org.example.util.enums.SUBJECT_TITLE;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;
    private Integer registrationCount = 0;

    @Enumerated(EnumType.STRING)
    private COURSE_STATUS courseStatus = COURSE_STATUS.OPEN;

    @CreationTimestamp
    private Date publishedDate;

    @Column(nullable = true)
    private Date startingDay;
    @Column(nullable = true)
    private Date endingDay;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "WishListCourses",
            joinColumns = @JoinColumn(name = "studentId"),
            inverseJoinColumns = @JoinColumn(name = "courseId")
    )
    private Set<Student> wishers = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private SUBJECT_TITLE subject;

    public Course() {
    }

    public Course(String name, String description, Date startingDay, Date endingDay, String subject, Teacher teacher) {
        this.name = name;
        this.description = description;
        this.startingDay = startingDay;
        this.endingDay = endingDay;
        this.subject = SUBJECT_TITLE.valueOf(subject);
        this.teacher = teacher;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getWishers() {
        return wishers;
    }

    public void setWishers(Set<Student> wishers) {
        this.wishers = wishers;
    }

    public SUBJECT_TITLE getSubject() {
        return subject;
    }

    public void setSubject(String title) {

        this.subject = SUBJECT_TITLE.valueOf(title);
    }
}
