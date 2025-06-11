package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.service.EmailService;
import com.duyhung.jobhunter.service.SubscriberService;
import com.duyhung.jobhunter.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send email")
//    @Scheduled(cron = "*/30 * * * * *")
//    @Transactional
    public String sendEmail() {
//        this.emailService.sendSimpleEmail();
//        this.emailService.sendEmailSync("test@gmail.com",
//                "Test send email",
//                "<h1> <b> Hello <b/> <h1/>",
//                false, true);
//        this.emailService.sendEmailFromTemplateSync("test@gmail.com", "Tuyển dụng Web Developer","job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
}