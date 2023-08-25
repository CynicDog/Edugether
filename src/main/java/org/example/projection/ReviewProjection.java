package org.example.projection;

import org.example.entity.users.User;
import org.example.util.enums.REVIEW_SENTIMENT;

import java.math.BigInteger;
import java.util.Date;

public class ReviewProjection {

    private Long id;
    private User writer;
    private Long courseTeacherId;
    private REVIEW_SENTIMENT reviewSentiment;
    private Date createDate;
    private String content;
    private long likedCount;
    private long reviewNumber;
    private BigInteger likedByTeacher;

    public ReviewProjection(Long id, User writer, Long courseTeacherId, REVIEW_SENTIMENT reviewSentiment, Date createDate, String content, long likedCount, long reviewNumber) {
        this.id = id;
        this.writer = writer;
        this.courseTeacherId = courseTeacherId;
        this.reviewSentiment = reviewSentiment;
        this.createDate = createDate;
        this.content = content;
        this.likedCount = likedCount;
        this.reviewNumber = reviewNumber;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Long getCourseTeacherId() {
        return courseTeacherId;
    }

    public REVIEW_SENTIMENT getReviewSentiment() {
        return reviewSentiment;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getContent() {
        return content;
    }

    public long getLikedCount() {
        return likedCount;
    }

    public long getReviewNumber() {
        return reviewNumber;
    }

    public BigInteger getLikedByTeacher() {
        return likedByTeacher;
    }

    public void setLikedByTeacher(BigInteger likedByTeacher) {
        this.likedByTeacher = likedByTeacher;
    }
}
