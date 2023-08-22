package org.example.entity.socials;

import org.example.entity.users.Student;
import org.example.util.enums.EVENT_STATUS;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class SocietyEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    @CreationTimestamp
    private Date heldDate;

    @ManyToOne
    @JoinColumn(name = "societyId")
    private Society society;

    @ManyToMany
    @JoinTable(
            name = "StudentSocietyEventAttendance",
            joinColumns = @JoinColumn(name = "eventId"),
            inverseJoinColumns = @JoinColumn(name = "studentId")
    )
    private List<Student> attendants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EVENT_STATUS eventStatus;

    public SocietyEvent() {
    }

    public SocietyEvent(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getHeldDate() {
        return heldDate;
    }

    public void setHeldDate(Date heldDate) {
        this.heldDate = heldDate;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public List<Student> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<Student> attendants) {
        this.attendants = attendants;
    }

    public EVENT_STATUS getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EVENT_STATUS eventStatus) {
        this.eventStatus = eventStatus;
    }
}
