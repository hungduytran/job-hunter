package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.service.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<String> home() throws IdInvalidException {
        //throw new IdInvalidException("check");
        return ResponseEntity.status(HttpStatus.OK).body("Hello World");
    }
}
