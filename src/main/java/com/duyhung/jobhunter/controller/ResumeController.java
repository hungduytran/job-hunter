package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.Company;
import com.duyhung.jobhunter.domain.Job;
import com.duyhung.jobhunter.domain.Resume;
import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.duyhung.jobhunter.domain.response.resume.ResFetchResumeDTO;
import com.duyhung.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.duyhung.jobhunter.service.ResumeService;
import com.duyhung.jobhunter.service.UserService;
import com.duyhung.jobhunter.util.SecurityUtil;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;

    public ResumeController(ResumeService resumeService, UserService userService, FilterSpecificationConverter filterSpecificationConverter, FilterBuilder filterBuilder) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterSpecificationConverter = filterSpecificationConverter;
        this.filterBuilder = filterBuilder;
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

        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.findByUsername(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);

        return ResponseEntity.ok().body(this.resumeService.fetchAllResumes(finalSpec, pageable));
    }

    @GetMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        ResultPaginationDTO result = this.resumeService.fetchResumeByUser(pageable);
        return ResponseEntity.ok(result);
    }


}
