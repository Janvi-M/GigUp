package com.gigup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer rating;
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "review_date")
    private LocalDateTime reviewDate;
    
    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;
    
    @ManyToOne
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    
    @PrePersist
    protected void onCreate() {
        reviewDate = LocalDateTime.now();
    }
}

