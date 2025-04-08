package com.gigup.repository;

import com.gigup.model.Bid;
import com.gigup.model.Project;
import com.gigup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByProject(Project project);
    List<Bid> findByFreelancer(User freelancer);
    List<Bid> findByProjectAndStatus(Project project, String status);
}

