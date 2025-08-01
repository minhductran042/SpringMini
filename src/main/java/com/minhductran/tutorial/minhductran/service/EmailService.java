package com.minhductran.tutorial.minhductran.service;


import com.minhductran.tutorial.minhductran.dto.response.EmailResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmailService {
    public ResponseEntity<EmailResponse> sendEmail(String to, String subject, String text);
    public String verifyEmail(String to, String name);
    public ResponseEntity<List<EmailResponse>> getAllEmails();
    public ResponseEntity<EmailResponse> getEmailById(int id);
}
