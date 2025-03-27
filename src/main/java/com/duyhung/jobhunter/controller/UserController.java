package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.domain.dto.ResultPaginationDTO;
import com.duyhung.jobhunter.service.UserService;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User currUser =  this.userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(currUser);
    }




    @DeleteMapping("/users/{id}")
    public ResponseEntity<String>  deleteUser(@PathVariable("id") Long id)
            throws IdInvalidException {
        if ( id > 1500 ) {
            throw new IdInvalidException("ID khong lon hon 1500");
        }
        this.userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted User");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = this.userService.findById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {
        String sCurrentPage = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        int currentPage = Integer.parseInt(sCurrentPage) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        return ResponseEntity.ok(this.userService.findAll(pageable));
//      return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.update(user));
    }

}
