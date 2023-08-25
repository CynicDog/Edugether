package org.example.projection;

import org.example.entity.users.User;
import org.example.util.enums.REVIEW_SENTIMENT;

import java.util.Date;

public class ReviewProjection {

    private Long id;
    private User user;
    private REVIEW_SENTIMENT reviewSentiment;
    private Date createDate;
    private String content;
    private long likedCount;
    private long reviewNumber;

    public ReviewProjection(Long id, User user, REVIEW_SENTIMENT reviewSentiment, Date createDate, String content, long likedCount, long reviewNumber) {
        this.id = id;
        this.user = user;
        this.reviewSentiment = reviewSentiment;
        this.createDate = createDate;
        this.content = content;
        this.likedCount = likedCount;
        this.reviewNumber = reviewNumber;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
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
}
