package com.duyhung.jobhunter.repository;

import com.duyhung.jobhunter.domain.Resume;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ResumeRepository extends CrudRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {
}
