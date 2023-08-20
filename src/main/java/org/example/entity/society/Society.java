package org.example.entity.society;

import org.example.entity.users.Student;
import org.example.entity.users.Teacher;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Society {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "SocietyMembers",
            joinColumns = @JoinColumn(name = "society_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> members = new HashSet<>();

    @CreationTimestamp
    private Date createDate;
}

