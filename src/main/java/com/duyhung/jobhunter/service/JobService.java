package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Company;
import com.duyhung.jobhunter.domain.Job;
import com.duyhung.jobhunter.domain.Job;
import com.duyhung.jobhunter.domain.Skill;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.domain.response.job.ResCreateJobDTO;
import com.duyhung.jobhunter.domain.response.job.ResUpdateJobDTO;
import com.duyhung.jobhunter.repository.CompanyRepository;
import com.duyhung.jobhunter.repository.JobRepository;
import com.duyhung.jobhunter.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;
    public JobService(JobRepository jobRepository, SkillRepository skillRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public Optional<Job> findById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResCreateJobDTO createJob(Job job) {
        //check skill
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findAllById(reqSkills);
            job.setSkills(dbSkills);
        }

        //check company
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                job.setCompany(companyOptional.get());
            }
        }

        //create job
        Job currentJob = this.jobRepository.save(job);

        //convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartAt(job.getStartAt());
        dto.setEndDate(job.getEndDate());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setCreatedBy(job.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;
    }

    public ResUpdateJobDTO updateJob(Job job, Job jobInDB) {
        //check skill
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findAllById(reqSkills);
            jobInDB.setSkills(dbSkills);
        }

        //check company
        if (job.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if (companyOptional.isPresent()) {
                jobInDB.setCompany(companyOptional.get());
            }
        }

        //update correct job
        jobInDB.setName(job.getName());
        jobInDB.setSalary(job.getSalary());
        jobInDB.setQuantity(job.getQuantity());
        jobInDB.setLocation(job.getLocation());
        jobInDB.setLevel(job.getLevel());
        jobInDB.setStartAt(job.getStartAt());
        jobInDB.setEndDate(job.getEndDate());
        jobInDB.setActive(job.isActive());

        //update job
        Job currentJob = this.jobRepository.save(jobInDB);

        //convert response
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartAt(job.getStartAt());
        dto.setEndDate(job.getEndDate());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setCreatedBy(job.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;
    }

    public ResultPaginationDTO getAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob =this.jobRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageJob.getNumber() + 1);
        mt.setPageSize(pageJob.getSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());

        return rs;
    }

    public void deleteJob(long id) {
        this.jobRepository.deleteById(id);
    }
}
