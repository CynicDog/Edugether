package org.example.entity.users;

import org.example.entity.socials.Society;
import org.example.util.enums.RELATIONSHIP;
import org.example.util.enums.TYPE;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "Interest",
            joinColumns = @JoinColumn(name = "studentId")
    )
    @Column(name = "title")
    private List<String> interests;

    @ManyToMany(mappedBy = "members")
    private List<Society> societies = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RELATIONSHIP relationship;

    public Student() {
    }

    public Student(String username, String password, String email, TYPE type) {
        super(username, password, email, type);
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<Society> getSocieties() {
        return societies;
    }

    public void setSocieties(List<Society> societies) {
        this.societies = societies;
    }

    public RELATIONSHIP getRelationship() {
        return relationship;
    }

    public void setRelationship(RELATIONSHIP relationship) {
        this.relationship = relationship;
    }
}
