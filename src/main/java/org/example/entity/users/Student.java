package org.example.entity.users;

import org.example.util.enums.RELATIONSHIP;
import org.example.util.enums.TYPE;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Student extends User {

    @ElementCollection
    @CollectionTable(
            name = "Interest",
            joinColumns = @JoinColumn(name = "studentId")
    )
    @Column(name = "title")
    private Set<String> interests;

    @Enumerated(EnumType.STRING)
    private RELATIONSHIP relationship;

    public Student() {
    }

    public Student(String username, String password, String email, TYPE type) {
        super(username, password, email, type);
    }

    public Set<String> getInterests() {
        return interests;
    }

    public void setInterests(Set<String> interests) {
        this.interests = interests;
    }

    public RELATIONSHIP getRelationship() {
        return relationship;
    }

    public void setRelationship(RELATIONSHIP relationship) {
        this.relationship = relationship;
    }
}
