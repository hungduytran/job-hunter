package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.domain.dto.ResCreateUserDTO;
import com.duyhung.jobhunter.domain.dto.ResUpdateUserDTO;
import com.duyhung.jobhunter.domain.dto.ResUserDTO;
import com.duyhung.jobhunter.domain.dto.ResultPaginationDTO;
import com.duyhung.jobhunter.service.UserService;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User user)
            throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email" + user.getEmail() + " da ton tai, vui long su dung email khac!");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User currUser =  this.userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateDTO(currUser));
    }




    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<String>  deleteUser(@PathVariable("id") Long id)
            throws IdInvalidException {
        User currUser = this.userService.findById(id);
        if ( currUser == null ) {
            throw new IdInvalidException("User voi id = " + id + " khong ton tai!");
        }
        this.userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted User");
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id)
            throws IdInvalidException {
        User fetchUser = this.userService.findById(id);
        if ( fetchUser == null ) {
            throw new IdInvalidException("User id = " + id + " khong ton tai!");
        }
        return  ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable
//            @RequestParam("current") Optional<String> currentOptional,
//            @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {
//        String sCurrentPage = currentOptional.isPresent() ? currentOptional.get() : "";
//        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
//
//        int currentPage = Integer.parseInt(sCurrentPage);
//        int pageSize = Integer.parseInt(sPageSize);
//
//        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        return ResponseEntity.ok(this.userService.findAll(spec, pageable));
//      return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User currUser = this.userService.update(user);
        if ( currUser == null ) {
            throw new IdInvalidException("User id = " + user.getId() + " khong ton tai!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(user));
    }

}
