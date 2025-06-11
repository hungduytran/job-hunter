package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Skill;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public boolean isNameExists(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill findSkillById(Long id) {
        Optional<Skill> skill = this.skillRepository.findById(id);
        if (skill.isPresent()) {
            return skill.get();
        }
        return null;
    }

    public Skill updateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public ResultPaginationDTO getAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill =this.skillRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageSkill.getNumber() + 1);
        mt.setPageSize(pageSkill.getSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());

        return rs;
    }

    public void deleteSkillById(Long id) {
        //delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(j -> j.getSkills().remove(currentSkill));

        //delete subscriber (inside subscriber_skill table)
        currentSkill.getSubscribers().forEach(sub -> sub.getSkills().remove(currentSkill));

        //delete skill
        this.skillRepository.deleteById(id);
    }
}
