package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.Subscriber;
import com.duyhung.jobhunter.service.SubscriberService;
import com.duyhung.jobhunter.util.SecurityUtil;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import com.duyhung.jobhunter.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber) throws IdInvalidException {
        //check email
        boolean isExist = subscriberService.existsByEmail(subscriber.getEmail());
        if (isExist == true) {
                throw new IdInvalidException("Email" + subscriber.getEmail() + " already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.createSubscriber(subscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) throws IdInvalidException {
        //check id
        Subscriber subsDB = this.subscriberService.findById(subscriber.getId());
        if (subsDB == null) {
            throw new IdInvalidException("Subscriber" + subscriber.getId() + "not found");
        }
        return ResponseEntity.ok().body(this.subscriberService.updateSubscriber(subsDB, subscriber));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscriberSkills() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }
}
