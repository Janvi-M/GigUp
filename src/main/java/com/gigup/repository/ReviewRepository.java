package com.gigup.repository;

import com.gigup.model.Project;
import com.gigup.model.Review;
import com.gigup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewee(User reviewee);
    List<Review> findByReviewer(User reviewer);
    List<Review> findByProject(Project project);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee = :reviewee")
    Double getAverageRatingForUser(User reviewee);
}

