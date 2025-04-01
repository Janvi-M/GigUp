package com.gigup.service;

import com.gigup.model.Bid;
import com.gigup.model.Project;
import com.gigup.model.User;
import com.gigup.repository.BidRepository;
import com.gigup.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {
    
    @Autowired
    private BidRepository bidRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    public List<Bid> findAllBids() {
        return bidRepository.findAll();
    }
    
    public Optional<Bid> findById(Long id) {
        return bidRepository.findById(id);
    }
    
    public List<Bid> findByProject(Project project) {
        return bidRepository.findByProject(project);
    }
    
    public List<Bid> findByFreelancer(User freelancer) {
        return bidRepository.findByFreelancer(freelancer);
    }
    
    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }
    
    public Bid updateBid(Bid bid) {
        return bidRepository.save(bid);
    }
    
    public void deleteBid(Long id) {
        bidRepository.deleteById(id);
    }
    
    @Transactional
    public void acceptBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));
        
        // Update bid status
        bid.setStatus("ACCEPTED");
        bidRepository.save(bid);
        
        // Update project status and assign freelancer
        Project project = bid.getProject();
        project.setStatus("IN_PROGRESS");
        project.setFreelancer(bid.getFreelancer());
        projectRepository.save(project);
        
        // Reject all other bids for this project
        List<Bid> otherBids = bidRepository.findByProjectAndStatus(project, "PENDING");
        for (Bid otherBid : otherBids) {
            if (!otherBid.getId().equals(bidId)) {
                otherBid.setStatus("REJECTED");
                bidRepository.save(otherBid);
            }
        }
    }
}

