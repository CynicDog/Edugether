package org.example.entity.users;

import org.example.util.enums.TYPE;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Teacher extends User{

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "Qualification",
            joinColumns = @JoinColumn(name = "teacherId")
    )
    @Column(name = "title")
    private Set<String> qualifications;

    private Integer salary;

    public Teacher() {
    }

    public Teacher(String username, String password, String email, TYPE type) {
        super(username, password, email, type);
    }

    public Set<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(Set<String> qualifications) {
        this.qualifications = qualifications;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
