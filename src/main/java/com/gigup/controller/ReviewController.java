package com.gigup.controller;

import com.gigup.model.Project;
import com.gigup.model.Review;
import com.gigup.model.User;
import com.gigup.service.ProjectService;
import com.gigup.service.ReviewService;
import com.gigup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping("/create/{projectId}/{revieweeId}")
    public String showCreateForm(@PathVariable Long projectId,
                                @PathVariable Long revieweeId,
                                Model model) {
        
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        User reviewee = userService.findById(revieweeId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("project", project);
        model.addAttribute("reviewee", reviewee);
        model.addAttribute("review", new Review());
        
        return "reviews/create";
    }
    
    @PostMapping("/create")
    public String createReview(@ModelAttribute Review review,
                              @RequestParam Long projectId,
                              @RequestParam Long revieweeId,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        
        User reviewer = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        User reviewee = userService.findById(revieweeId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        // Check if project is completed
        if (!"COMPLETED".equals(project.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "You can only review completed projects");
            return "redirect:/projects/" + projectId;
        }
        
        // Check if reviewer is either the client or the freelancer of the project
        if (!project.getClient().getId().equals(reviewer.getId()) && 
            !project.getFreelancer().getId().equals(reviewer.getId())) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to review this project");
            return "redirect:/projects/" + projectId;
        }
        
        // Check if reviewee is either the client or the freelancer of the project
        if (!project.getClient().getId().equals(reviewee.getId()) && 
            !project.getFreelancer().getId().equals(reviewee.getId())) {
            redirectAttributes.addFlashAttribute("error", "Invalid reviewee");
            return "redirect:/projects/" + projectId;
        }
        
        // Check if reviewer is not reviewing themselves
        if (reviewer.getId().equals(reviewee.getId())) {
            redirectAttributes.addFlashAttribute("error", "You cannot review yourself");
            return "redirect:/projects/" + projectId;
        }
        
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setProject(project);
        
        reviewService.createReview(review);
        
        redirectAttributes.addFlashAttribute("success", "Review submitted successfully");
        return "redirect:/projects/" + projectId;
    }
}

