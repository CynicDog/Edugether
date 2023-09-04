package org.example.projection;

import org.example.util.enums.FOLLOW_REQUEST_STATUS;
import org.example.util.enums.TYPE;

public class UserProjection {

    private Long id;
    private String username;
    private String email;
    private TYPE type;
    private Long followRequestId;
    private FOLLOW_REQUEST_STATUS requestStatus;


    public UserProjection(Long id, String username, TYPE type) {
        this.id = id;
        this.username = username;
        this.type = type;
    }

    public UserProjection(Long id, String username, String email, TYPE type) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.type = type;
    }

    public UserProjection(Long id, String username, TYPE type, Long followRequestId, FOLLOW_REQUEST_STATUS requestStatus) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.followRequestId = followRequestId;
        this.requestStatus = requestStatus;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public TYPE getType() {
        return type;
    }

    public FOLLOW_REQUEST_STATUS getRequestStatus() {
        return requestStatus;
    }

    public Long getFollowRequestId() {
        return followRequestId;
    }

    public void setRequestStatus(FOLLOW_REQUEST_STATUS requestStatus) {
        this.requestStatus = requestStatus;
    }
}
