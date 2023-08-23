package org.example.entity.academics;

import org.example.entity.users.Student;
import org.example.util.enums.REGISTRATION_STATUS;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "courseId"}))
public class Registration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @Enumerated(EnumType.STRING)
    private REGISTRATION_STATUS registrationStatus = REGISTRATION_STATUS.ENROLLED;

    @CreationTimestamp
    private Date enrolledDate;

    public Registration() {
    }

    public Registration(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public REGISTRATION_STATUS getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(REGISTRATION_STATUS registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public Date getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
    }


}
