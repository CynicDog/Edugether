package org.example.entity.academics;

import org.example.entity.users.User;
import org.example.util.enums.REVIEW_SENTIMENT;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "userId", "courseId" }))
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @Enumerated(EnumType.STRING)
    private REVIEW_SENTIMENT reviewSentiment;

    @CreationTimestamp
    private Date createDate;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "LikedReviews",
            joinColumns = @JoinColumn(name = "reviewId"),
            inverseJoinColumns = @JoinColumn(name = "userId"),
            uniqueConstraints = @UniqueConstraint(columnNames = { "reviewId", "userId" })
    )
    private Set<User> likers;

    public Review() {
    }

    public Review(User writer, Course course, REVIEW_SENTIMENT reviewSentiment, String content) {
        this.writer = writer;
        this.course = course;
        this.reviewSentiment = reviewSentiment;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User user) {
        this.writer = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public REVIEW_SENTIMENT getReviewSentiment() {
        return reviewSentiment;
    }

    public void setReviewSentiment(REVIEW_SENTIMENT reviewSentiment) {
        this.reviewSentiment = reviewSentiment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Set<User> getLikers() {
        return likers;
    }

    public void setLikers(Set<User> likers) {
        this.likers = likers;
    }

    public void addLikers(User user) {
        if (this.likers == null) {
            this.likers = new HashSet<>();
        }
        this.likers.add(user);
    }
}
