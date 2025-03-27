package com.duyhung.jobhunter.repository;

import com.duyhung.jobhunter.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> id(long id);
}
