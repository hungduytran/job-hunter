package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Company;
import com.duyhung.jobhunter.domain.Role;
import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.repository.CompanyRepository;
import com.duyhung.jobhunter.service.CompanyService;
import com.duyhung.jobhunter.domain.response.ResCreateUserDTO;
import com.duyhung.jobhunter.domain.response.ResUpdateUserDTO;
import com.duyhung.jobhunter.domain.response.ResUserDTO;
import com.duyhung.jobhunter.domain.response.ResultPaginationDTO;
import com.duyhung.jobhunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final RoleService roleService;


    public UserService(UserRepository userRepository, CompanyService companyService, CompanyRepository companyRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.roleService = roleService;
    }



    public User save(User user) {
        //check company
        if(user.getCompany() != null){
            Optional<Company> companyOptional = this.companyService.findCompanyById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }

        //check role
        if(user.getRole() != null){
            Role role = this.roleService.findById(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }
        return this.userRepository.save(user);
    }

    public void deleteById(Long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if(companyOptional.isPresent()){
            Company company = companyOptional.get();
            //fetch all user belong to this company
            List<User> users = this.userRepository.findByCompany(company);
            this.userRepository.deleteAll(users);
        }
        this.userRepository.deleteById(id);
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResultPaginationDTO findAll(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers =this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUsers.getTotalPages());
        mt.setTotal(pageUsers.getTotalElements());

        rs.setMeta(mt);

        //remove sensitive data
        List<ResUserDTO> listUser = pageUsers.getContent()
                .stream()
                .map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());


        rs.setResult(listUser);

        return rs;
    }

    public User findByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public User update(User reqUser) {
        User currentUser = this.findById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setAge(reqUser.getAge());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAddress(reqUser.getAddress());

            //check company
            if (reqUser.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findCompanyById(reqUser.getCompany().getId());
                currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            }

            //check role
            if (reqUser.getRole() != null) {
                Role role = this.roleService.findById(reqUser.getRole().getId());
                currentUser.setRole(role != null ? role : null);
            }

            //update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompanyUser(companyUser);
        }

        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setUpdateAt(user.getUpdatedAt());
        return res;
    }

    public ResCreateUserDTO convertToResCreateDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();

        if(user.getCompany() != null){
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompanyUser(companyUser);
        }

        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());


        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompanyUser(companyUser);
        }

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            res.setRoleUser(roleUser);
        }

        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());

        if(user.getCompany() != null){
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompanyUser(companyUser);
        }

        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.findByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getuserByRefreshTokenAndEmail(String Token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(Token, email);
    }
}
