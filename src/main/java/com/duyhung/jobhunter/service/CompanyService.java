package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Company;
import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.repository.CompanyRepository;
import com.duyhung.jobhunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company createCompany(Company company){
        return companyRepository.save(company);
    }

    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable){
        Page<Company> pageCompany =this.companyRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company updateCompany(Company company){
        Optional<Company> companyToUpdate = companyRepository.findById(company.getId());
        if(companyToUpdate.isPresent()){
            Company currentCompany = companyToUpdate.get();
            currentCompany.setName(company.getName());
            currentCompany.setAddress(company.getAddress());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setLogo(company.getLogo());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    public Company findCompanyById(Long id){
        Optional<Company> company = this.companyRepository.findById(id);
        if (company.isPresent()) {
            return company.get();
        }
        return null;
    }

    public void deleteCompany(Long id){
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            //fetch all user  belong to this company
            List<User> users = this.userRepository.findByCompany(company);
            this.userRepository.deleteAll(users);
        }
        companyRepository.deleteById(id);
    }

    public Optional<Company> findCompanyById(long id){
        return this.companyRepository.findById(id);
    }
}
