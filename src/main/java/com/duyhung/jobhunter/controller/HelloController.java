package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.service.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() throws IdInvalidException {
        //throw new IdInvalidException("check");
        return "Hello World️";
    }
}
