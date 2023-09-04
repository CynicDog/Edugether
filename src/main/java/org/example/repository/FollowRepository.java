package org.example.repository;

import org.example.entity.socials.Follow;

public interface FollowRepository {
    void insertFollow(Follow follow);
    void deleteFollow(Follow follow);
    Follow getFollowByFollowerAndFollowed(Long senderId, Long recipientId);
}
