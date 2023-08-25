package org.example.repository;

import org.example.entity.academics.Review;
import org.example.projection.ReviewProjection;

import java.math.BigInteger;
import java.util.List;

public interface ReviewRepository {

    void insertReview(Review review);

    List<ReviewProjection> getPaginatedReviewsByCourseIdAndCreateDateDescending(Long courseId, Integer page, Integer limit);
    Review getReviewById(Long reviewId);
    BigInteger getReviewLikedCount(Long reviewId, Long userId);
}
