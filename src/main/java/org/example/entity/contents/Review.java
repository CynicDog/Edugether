package org.example.entity.contents;

import org.example.entity.academics.Course;
import org.example.entity.users.User;
import org.example.util.enums.REVIEW_SENTIMENT;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private Integer likedCount = 0;

    public Review() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
