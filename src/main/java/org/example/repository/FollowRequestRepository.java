package org.example.repository;

import org.example.entity.socials.FollowRequest;
import org.example.projection.UserProjection;

import java.util.List;

public interface FollowRequestRepository {
    void insertFollowRequest(FollowRequest followRequest);

    List<UserProjection> getPendingRequestsByRecipientId(Long recipientId, Integer page, Integer limit);

    List<UserProjection> getAcceptedRequestsByRecipientId(Long recipientId, Integer page, Integer limit);

    List<UserProjection> getDeclinedRequestsByRecipientId(Long recipientId, Integer page, Integer limit);

    List<UserProjection> getSentRequestsByRecipientId(Long recipientId, Integer page, Integer limit);

    FollowRequest getRequestById(Long requestId);

    void updateFollowRequest(FollowRequest followRequest);
}
