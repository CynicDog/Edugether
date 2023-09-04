package org.example.repository;

import org.example.entity.socials.Follow;
import org.example.entity.users.User;

import java.util.List;

public interface FollowRepository {
    void insertFollow(Follow follow);
    void deleteFollow(Follow follow);
    Follow getFollowByFollowerAndFollowed(Long senderId, Long recipientId);
    long countFollowByFollowedId(Long userId);
    long countFollowByFollowerId(Long userId);
    
    List<User> getFollowersPaginatedByUserIdOrderByCreateDateAsc(Long userId, Integer page, Integer limit);
    List<User> getFollowersPaginatedByUserIdOrderByCreateDateDesc(Long userId, Integer page, Integer limit);
    List<User> getFollowingsPaginatedByUserIdOrderByCreateDateAsc(Long userId, Integer page, Integer limit);
    List<User> getFollowingsPaginatedByUserIdOrderByCreateDateDesc(Long userId, Integer page, Integer limit);
}
