package com.duyhung.jobhunter.controller;


import com.duyhung.jobhunter.domain.Company;
import com.duyhung.jobhunter.domain.dto.ResultPaginationDTO;
import com.duyhung.jobhunter.service.CompanyService;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.companyService.createCompany(company));
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional
    ){
        String sCurrentPage = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        int currentPage = Integer.parseInt(sCurrentPage) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        return ResponseEntity.ok(this.companyService.getAllCompanies(pageable));
    }




    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.updateCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id)
            throws IdInvalidException {
        if ( id > 1500 ) {
            throw new IdInvalidException("ID khong lon hon 1500");
        }
        this.companyService.deleteCompany(id);
//        return ResponseEntity.status(HttpStatus.OK).body();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
