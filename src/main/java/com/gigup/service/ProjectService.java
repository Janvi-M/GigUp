package com.gigup.service;

import com.gigup.model.Project;
import com.gigup.model.Skill;
import com.gigup.model.User;
import com.gigup.repository.ProjectRepository;
import com.gigup.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }
    
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }
    
    public List<Project> findByStatus(String status) {
        return projectRepository.findByStatus(status);
    }
    
    public List<Project> findByClient(User client) {
        return projectRepository.findByClient(client);
    }
    
    public List<Project> findByFreelancer(User freelancer) {
        return projectRepository.findByFreelancer(freelancer);
    }
    
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
    
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }
    
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    
    public Project addSkillsToProject(Long projectId, Set<Long> skillIds) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        for (Long skillId : skillIds) {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new RuntimeException("Skill not found"));
            project.getSkills().add(skill);
        }
        
        return projectRepository.save(project);
    }
    
    public List<Project> findOpenProjectsBySkill(Long skillId) {
        return projectRepository.findOpenProjectsBySkill(skillId);
    }
    
    public Project completeProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        // Check if project is in progress
        if (!"IN_PROGRESS".equals(project.getStatus())) {
            throw new RuntimeException("Only in-progress projects can be marked as completed");
        }
        
        // Update project status to COMPLETED
        project.setStatus("COMPLETED");
        return projectRepository.save(project);
    }
}

