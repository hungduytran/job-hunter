package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.User;
import com.duyhung.jobhunter.service.UserService;
import com.duyhung.jobhunter.service.error.IdInvalidException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
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
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(this.userService.findAll());
//      return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.update(user));
    }
}
