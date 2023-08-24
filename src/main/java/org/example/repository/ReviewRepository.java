package org.example.repository;

import org.example.entity.contents.Review;
import org.example.projection.ReviewProjection;

import java.util.List;

public interface ReviewRepository {

    void insertReview(Review review);

    List<ReviewProjection> getPaginatedReviewsByCourseIdAndCreateDateDescending(Long courseId, Integer page, Integer limit);
}
