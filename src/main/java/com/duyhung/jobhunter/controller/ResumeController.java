package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.Resume;
import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.duyhung.jobhunter.domain.response.resume.ResFetchResumeDTO;
import com.duyhung.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.duyhung.jobhunter.service.ResumeService;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {
        //check id
        boolean isExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isExist) {
            throw new IdInvalidException("User/Job does not exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
        //check id exist
        Optional<Resume> resumeOptional = this.resumeService.findById(resume.getId());
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id " + resume.getId() + " does not exist");
        }

        Resume reqResume = resumeOptional.get();
        reqResume.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(this.resumeService.update(reqResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume by id")
    public ResponseEntity<Void> delete(@PathVariable long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.findById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id " + id + " does not exist");
        }

        this.resumeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> findById(@PathVariable long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.findById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id " + id + " does not exist");
        }

        return ResponseEntity.ok().body(this.resumeService.getResume(resumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
            @Filter Specification<Resume> spec,
            Pageable pageable) throws IdInvalidException {

        return ResponseEntity.ok().body(this.resumeService.fetchAllResumes(spec, pageable));
    }

    @GetMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        ResultPaginationDTO result = this.resumeService.fetchResumeByUser(pageable);
        return ResponseEntity.ok(result);
    }


}
