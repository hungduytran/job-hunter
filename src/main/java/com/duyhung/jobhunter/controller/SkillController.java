package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.Skill;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.service.SkillService;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        //check name
        if (skill.getName() != null && this.skillService.isNameExists(skill.getName())) {
            throw new IdInvalidException("Skill name" + skill.getName() + "da ton tai!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(skill));
    }

    @PutMapping("/skills/{id}")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        //check id
        Skill currentSkill = this.skillService.findSkillById(skill.getId());
        if(currentSkill == null) {
            throw new IdInvalidException("Skill id " + skill.getId() + " khong ton tai!");
        }

        //check name
//        if (currentSkill.getName() != null && this.skillService.isNameExists(currentSkill.getName())) {
//            throw new IdInvalidException("Skill name " + skill.getName() + " da ton tai!");
//        }

        String newName = skill.getName();
        if (newName == null || newName.trim().isEmpty()) {
            throw new IdInvalidException("Skill name khong duoc de trong!");
        }

        // Nếu tên mới giống tên cũ → lỗi
        if (newName.equalsIgnoreCase(currentSkill.getName())) {
            throw new IdInvalidException("Skill name " + newName + " da ton tai!");
        }

        // Nếu tên mới đã tồn tại ở skill khác → lỗi
        if (this.skillService.isNameExists(newName)) {
            throw new IdInvalidException("Skill name " + newName + " da ton tai!");
        }

        currentSkill.setName(skill.getName());
        return ResponseEntity.ok().body(this.skillService.updateSkill(currentSkill));

    }
    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.getAllSkills(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) throws IdInvalidException {
        //check id
        Skill skill = this.skillService.findSkillById(id);
        if(skill == null) {
            throw new IdInvalidException("Skill id " + id + " khong ton tai!");
        }
        this.skillService.deleteSkillById(id);
        return ResponseEntity.ok().body(null);
    }
}
