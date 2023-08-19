package org.example.entity.socials;

import org.example.util.enums.FOLLOW_REQUEST_STATUS;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "senderId", "recipientId" }))
public class FollowRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer senderId;
    private Integer recipientId;

    @Enumerated(EnumType.STRING)
    private FOLLOW_REQUEST_STATUS followRequestStatus;

    public FollowRequest() {
    }

    public FollowRequest(Integer senderId, Integer recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public Long getId() {
        return id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public FOLLOW_REQUEST_STATUS getFollowRequestStatus() {
        return followRequestStatus;
    }

    public void setFollowRequestStatus(FOLLOW_REQUEST_STATUS followRequestStatus) {
        this.followRequestStatus = followRequestStatus;
    }
}
