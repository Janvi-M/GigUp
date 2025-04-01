package com.gigup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private String email;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    
    private String role = "ROLE_USER";
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Project> clientProjects = new ArrayList<>();
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<Project> freelancerProjects = new ArrayList<>();
    
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();
    
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    private List<Review> givenReviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "reviewee", cascade = CascadeType.ALL)
    private List<Review> receivedReviews = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }
}

