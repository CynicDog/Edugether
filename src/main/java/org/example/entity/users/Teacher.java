package org.example.entity.users;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Teacher extends User{

    @ElementCollection
    @CollectionTable(
            name = "Qualification",
            joinColumns = @JoinColumn(name = "teacherId")
    )
    @Column(name = "title")
    private Set<String> qualifications;

    private Integer salary;

    public Teacher() {
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
