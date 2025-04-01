package com.gigup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "posted_date")
    private LocalDateTime postedDate;
    
    private LocalDate deadline;
    
    private BigDecimal budget;
    
    private String status = "OPEN";
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;
    
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private User freelancer;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "project_skills",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        postedDate = LocalDateTime.now();
    }
}

