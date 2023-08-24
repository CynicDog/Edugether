package org.example.entity.contents;

import org.example.entity.academics.Course;
import org.example.entity.users.User;
import org.example.util.enums.REVIEW_SENTIMENT;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @ElementCollection
    @CollectionTable(
            name = "ReviewComment",
            joinColumns = @JoinColumn(name = "userId")
    )
    @Column(name = "content")
    private List<String> comments;

    @Enumerated(EnumType.STRING)
    private REVIEW_SENTIMENT reviewSentiment;

    @CreationTimestamp
    private Date createDate;

    @Column(nullable = false, length = 2000)
    private String content;
    private Integer likedCount = 0;

    public Review() {
    }

    public Review(User user, Course course, REVIEW_SENTIMENT reviewSentiment, String content) {
        this.user = user;
        this.course = course;
        this.reviewSentiment = reviewSentiment;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
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

    public Integer getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(Integer likedCount) {
        this.likedCount = likedCount;
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
}
