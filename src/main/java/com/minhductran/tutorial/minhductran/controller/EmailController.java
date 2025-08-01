package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.response.EmailResponse;
import com.minhductran.tutorial.minhductran.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@Slf4j(topic = "Email-Controller")
@CrossOrigin
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<EmailResponse> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        log.info("Sending email to: {},", to);
        return emailService.sendEmail(to, subject, text);
    }

    @GetMapping("/verify-email")
    public void emailVerification(@RequestParam String to, @RequestParam String name) throws IOException {
        log.info("Verify email to: {},", to);
        emailService.verifyEmail(to, name);
        log.info("Email sent to: {},", to);
    }

    @GetMapping
    public ResponseEntity<List<EmailResponse>> getAllEmails() {
        log.info("Fetching all emails");
        return emailService.getAllEmails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailResponse> getEmailById(@PathVariable int id) {
        log.info("Fetching email with ID: {}", id);
        return emailService.getEmailById(id);
    }
}
