package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Job;
import com.duyhung.jobhunter.domain.Resume;
import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.duyhung.jobhunter.domain.response.resume.ResFetchResumeDTO;
import com.duyhung.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.duyhung.jobhunter.repository.JobRepository;
import com.duyhung.jobhunter.repository.ResumeRepository;
import com.duyhung.jobhunter.repository.UserRepository;
import com.duyhung.jobhunter.util.SecurityUtil;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;


    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, JobRepository jobRepository, UserService userService, UserRepository userRepository, FilterParser filterParser, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public Optional<Resume> findById(long id) {
        return resumeRepository.findById(id);
    }

    public ResCreateResumeDTO create(Resume resume){
        resume = this.resumeRepository.save(resume);

        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resume.getId());
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resume.getCreatedBy());

        return resCreateResumeDTO;
    }

    public ResUpdateResumeDTO update(Resume resume){
        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO resUpdateResumeDTO = new ResUpdateResumeDTO();
        resUpdateResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resUpdateResumeDTO.setUpdatedBy(resume.getUpdatedBy());
        return resUpdateResumeDTO;
    }

    public void delete(long id){
        this.resumeRepository.deleteById(id);
    }

//    public ResFetchResumeDTO getResume(Resume resume){
//        ResFetchResumeDTO resFetchResumeDTO = new ResFetchResumeDTO();
//        resFetchResumeDTO.setId(resume.getId());
//        resFetchResumeDTO.setEmail(resume.getEmail());
//        resFetchResumeDTO.setUrl(resume.getUrl());
//        resFetchResumeDTO.setStatus(resume.getStatus());
//        resFetchResumeDTO.setCreatedAt(resume.getCreatedAt());
//        resFetchResumeDTO.setCreatedBy(resume.getCreatedBy());
//        resFetchResumeDTO.setUpdatedAt(resume.getUpdatedAt());
//        resFetchResumeDTO.setUpdatedBy(resume.getUpdatedBy());
//
//        if (resume.getJob() != null) {
//            resFetchResumeDTO.setCompanyName(resume.getJob().getCompany().getName());
//        }
//
//        resFetchResumeDTO.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
//        resFetchResumeDTO.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
//        return resFetchResumeDTO;
//
//    }
public ResFetchResumeDTO getResume(Resume resume){
    ResFetchResumeDTO resFetchResumeDTO = new ResFetchResumeDTO();
    resFetchResumeDTO.setId(resume.getId());
    resFetchResumeDTO.setEmail(resume.getEmail());
    resFetchResumeDTO.setUrl(resume.getUrl());
    resFetchResumeDTO.setStatus(resume.getStatus());
    resFetchResumeDTO.setCreatedAt(resume.getCreatedAt());
    resFetchResumeDTO.setCreatedBy(resume.getCreatedBy());
    resFetchResumeDTO.setUpdatedAt(resume.getUpdatedAt());
    resFetchResumeDTO.setUpdatedBy(resume.getUpdatedBy());

    if (resume.getJob() != null) {
        resFetchResumeDTO.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        if (resume.getJob().getCompany() != null) {
            resFetchResumeDTO.setCompanyName(resume.getJob().getCompany().getName());
        } else {
            resFetchResumeDTO.setCompanyName(null);
        }
    } else {
        resFetchResumeDTO.setJob(null);
        resFetchResumeDTO.setCompanyName(null);
    }

    if (resume.getUser() != null) {
        resFetchResumeDTO.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
    } else {
        resFetchResumeDTO.setUser(null);
    }

    return resFetchResumeDTO;
}

    public ResultPaginationDTO fetchAllResumes(Specification<Resume> spec, Pageable pageable){
        Page<Resume> pageUser = this.resumeRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageUser.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume){
        if(resume.getUser() == null){
            return false;
        }
        Optional<User> user = this.userRepository.findById(resume.getUser().getId());
        if(user.isEmpty()){
            return false;
        }
        if(resume.getJob().getId() == null){
            return false;
        }
        Optional<Job> job = this.jobRepository.findById(resume.getJob().getId());
        if(job.isEmpty()){
            return false;
        }
        return true;
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // Lấy email user hiện tại đăng nhập
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        // Build filter cho email
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);

        // Query database theo filter và pageable
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        // Tạo đối tượng trả về
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // Chuyển Resume entity sang DTO
        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream()
                .map(this::getResume)  // method chuyển entity -> DTO
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }

}
