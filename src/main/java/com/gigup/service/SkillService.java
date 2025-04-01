package com.gigup.service;

import com.gigup.model.Skill;
import com.gigup.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    
    @Autowired
    private SkillRepository skillRepository;
    
    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }
    
    public Optional<Skill> findById(Long id) {
        return skillRepository.findById(id);
    }
    
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }
    
    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }
    
    public Skill updateSkill(Skill skill) {
        return skillRepository.save(skill);
    }
    
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
    
    public Skill findOrCreateSkill(String name) {
        return skillRepository.findByName(name)
                .orElseGet(() -> {
                    Skill newSkill = new Skill();
                    newSkill.setName(name);
                    return skillRepository.save(newSkill);
                });
    }
}

