package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.domain.dto.Meta;
import com.duyhung.jobhunter.domain.dto.ResultPaginationDTO;
import com.duyhung.jobhunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public ResultPaginationDTO findAll(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers =this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUsers.getTotalPages());
        mt.setTotal(pageUsers.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUsers.getContent());

        return rs;
    }

    public User findByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public User update(User reqUser) {
        User currentUser = this.findById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setEmail(reqUser.getEmail());
            currentUser.setPassword(reqUser.getPassword());
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }
}
