package com.gigup.service;

import com.gigup.model.Project;
import com.gigup.model.Review;
import com.gigup.model.User;
import com.gigup.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }
    
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }
    
    public List<Review> findByReviewee(User reviewee) {
        return reviewRepository.findByReviewee(reviewee);
    }
    
    public List<Review> findByReviewer(User reviewer) {
        return reviewRepository.findByReviewer(reviewer);
    }
    
    public List<Review> findByProject(Project project) {
        return reviewRepository.findByProject(project);
    }
    
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }
    
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }
    
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
    
    public Double getAverageRatingForUser(User user) {
        return reviewRepository.getAverageRatingForUser(user);
    }
}

