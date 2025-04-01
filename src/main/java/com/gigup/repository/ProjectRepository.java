package com.gigup.repository;

import com.gigup.model.Project;
import com.gigup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStatus(String status);
    List<Project> findByClient(User client);
    List<Project> findByFreelancer(User freelancer);
    
    @Query("SELECT p FROM Project p JOIN p.skills s WHERE s.id = :skillId AND p.status = 'OPEN'")
    List<Project> findOpenProjectsBySkill(Long skillId);
}

