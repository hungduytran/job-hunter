package com.duyhung.jobhunter.repository;

import com.duyhung.jobhunter.domain.Company;
import com.duyhung.jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    User save(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String token, String email);

    List<User> findByCompany(Company company);
}
