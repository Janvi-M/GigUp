package com.gigup.controller;

import com.gigup.model.Bid;
import com.gigup.model.Project;
import com.gigup.model.Skill;
import com.gigup.model.User;
import com.gigup.service.BidService;
import com.gigup.service.ProjectService;
import com.gigup.service.SkillService;
import com.gigup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

<<<<<<< HEAD
=======
import java.math.BigDecimal;
>>>>>>> feature/fix-bidding-and-filters
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SkillService skillService;
    
    @Autowired
    private BidService bidService;

    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.findByStatus("OPEN");
        model.addAttribute("projects", projects);
        return "projects/list";
    }
    
    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model, Authentication authentication) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        User currentUser = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Bid> bids = bidService.findByProject(project);
        
        model.addAttribute("project", project);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("bids", bids);
        model.addAttribute("newBid", new Bid());
        
        return "projects/view";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("skills", skillService.findAllSkills());
        return "projects/create";
    }
    
    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, 
                               @RequestParam(required = false) Set<Long> skillIds,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        
        User client = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        project.setClient(client);
        Project savedProject = projectService.createProject(project);
        
        if (skillIds != null && !skillIds.isEmpty()) {
            projectService.addSkillsToProject(savedProject.getId(), skillIds);
        }
        
        redirectAttributes.addFlashAttribute("success", "Project created successfully");
        return "redirect:/projects";
    }
    
    @GetMapping("/my-projects")
    public String myProjects(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Project> clientProjects = projectService.findByClient(user);
        List<Project> freelancerProjects = projectService.findByFreelancer(user);
        
        model.addAttribute("clientProjects", clientProjects);
        model.addAttribute("freelancerProjects", freelancerProjects);
        
        return "projects/my-projects";
    }
    
    @PostMapping("/{id}/bid")
    public String placeBid(@PathVariable Long id, 

                          @RequestParam("amount") String amountStr,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        
        try {
            Project project = projectService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            
            User freelancer = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Check if user is the project owner
            if (project.getClient().getId().equals(freelancer.getId())) {
                redirectAttributes.addFlashAttribute("error", "You cannot bid on your own project");
                return "redirect:/projects/" + id;
            }
            
            // Check if project is still open
            if (!"OPEN".equals(project.getStatus())) {
                redirectAttributes.addFlashAttribute("error", "This project is no longer accepting bids");
                return "redirect:/projects/" + id;
            }
            
            // Parse and validate bid amount
            if (amountStr == null || amountStr.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please enter a valid bid amount");
                return "redirect:/projects/" + id;
            }
            
            // Convert amount string to BigDecimal
            BigDecimal amount;
            try {
                amountStr = amountStr.replaceAll("[^0-9.]", "");
                amount = new BigDecimal(amountStr);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("error", "Invalid bid amount format. Please enter a valid number");
                return "redirect:/projects/" + id;
            }
            
            // Ensure positive amount
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("error", "Bid amount must be greater than zero");
                return "redirect:/projects/" + id;
            }
            
            // Check for existing bids from this freelancer for this project
            List<Bid> existingBids = bidService.findByProject(project);
            for (Bid existingBid : existingBids) {
                if (existingBid.getFreelancer().getId().equals(freelancer.getId()) && 
                    "PENDING".equals(existingBid.getStatus())) {
                    redirectAttributes.addFlashAttribute("error", "You already have a pending bid for this project");
                    return "redirect:/projects/" + id;
                }
            }
            
            // Create a new bid
            Bid bid = new Bid();
            bid.setAmount(amount);
            bid.setProject(project);
            bid.setFreelancer(freelancer);
            bid.setStatus("PENDING");
            
            bidService.createBid(bid);
            
            redirectAttributes.addFlashAttribute("success", "Bid placed successfully");
            return "redirect:/projects/" + id;  
        } catch (Exception e) {
            e.printStackTrace(); // Log the full exception details
            redirectAttributes.addFlashAttribute("error", "An error occurred while placing your bid: " + e.getMessage());
            return "redirect:/projects/" + id;
        }

    }
    
    @PostMapping("/bids/{bidId}/accept")
    public String acceptBid(@PathVariable Long bidId, 
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        Bid bid = bidService.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));
        
        User client = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user is the project owner
        if (!bid.getProject().getClient().getId().equals(client.getId())) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to accept this bid");
            return "redirect:/projects/" + bid.getProject().getId();
        }
        
        bidService.acceptBid(bidId);
        
        redirectAttributes.addFlashAttribute("success", "Bid accepted successfully");
        return "redirect:/projects/" + bid.getProject().getId();
    }
}

