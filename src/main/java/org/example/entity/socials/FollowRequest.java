package org.example.entity.socials;

import org.example.util.enums.FOLLOW_REQUEST_STATUS;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "senderId", "recipientId" }))
public class FollowRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private Long recipientId;

    @Enumerated(EnumType.STRING)
    private FOLLOW_REQUEST_STATUS followRequestStatus = FOLLOW_REQUEST_STATUS.PENDING;

    @CreationTimestamp
    private Date createDate = new Date();

    public FollowRequest() {
    }

    public FollowRequest(Long senderId, Long recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.followRequestStatus = FOLLOW_REQUEST_STATUS.PENDING;
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public FOLLOW_REQUEST_STATUS getFollowRequestStatus() {
        return followRequestStatus;
    }

    public void setFollowRequestStatus(FOLLOW_REQUEST_STATUS followRequestStatus) {
        this.followRequestStatus = followRequestStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
