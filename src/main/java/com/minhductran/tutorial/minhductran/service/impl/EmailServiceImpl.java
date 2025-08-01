package com.minhductran.tutorial.minhductran.service.impl;

import com.minhductran.tutorial.minhductran.dto.response.EmailResponse;
import com.minhductran.tutorial.minhductran.repository.EmailRepository;
import com.minhductran.tutorial.minhductran.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Email-Service")
public class EmailServiceImpl implements EmailService {

    @Value("${spring.sendgrid.from-email}")
    private String from;

    @Value("${spring.sendgrid.template_id}")
    private String templateId;

    @Value("${spring.sendgrid.verification_link}")
    private String verificationLink;

    private final EmailRepository emailRepository;

    private final SendGrid sendGrid;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<EmailResponse> sendEmail(String to, String subject, String text) {
        Email fromEmail = new Email(from); // Sender
        Email toEmail = new Email(to); // Recipient

        // Set up dynamic template data
        Map<String, String> dynamicTemplateData = new HashMap<>();
        dynamicTemplateData.put("subject", subject);
        dynamicTemplateData.put("text", text);

        // Create mail object
        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setSubject(subject);
        mail.setTemplateId("d-acefdab7cc174e269e404dd92c0e3c5f"); // Use dynamic template ID

        // Add personalization
        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);
        dynamicTemplateData.forEach(personalization::addDynamicTemplateData);
        mail.addPersonalization(personalization);

        try {
            // Create and send SendGrid request
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully to {}", to);

                // Save email details to the database
                com.minhductran.tutorial.minhductran.model.Email email = new com.minhductran.tutorial.minhductran.model.Email();
                email.setTo(to);
                email.setSubject(subject);
                email.setText(text);
                emailRepository.save(email);

                return new ResponseEntity<>(
                        EmailResponse.builder()
                                .id(email.getId())
                                .to(to)
                                .subject(subject)
                                .text(text)
                                .build(),
                        HttpStatus.OK
                );
            } else {
                log.error("Failed to send email to {}. Status code: {}, Body: {}",
                        to, response.getStatusCode(), response.getBody());
                throw new IOException("Failed to send email. Status code: " + response.getStatusCode());
            }

        } catch (IOException e) {
            log.error("Error sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Error sending email", e);
        }
    }

    @Override
    public String verifyEmail(String to, String name) {
        try {
            log.info("Email verification started");
            Email fromEmail = new Email(from, "Trần Minh Đức"); // nguoi gui
            Email toEmail = new Email(to); // nguoi nhan

            String subject = "Xác thực tài khoản";

            String verficationCode = generateVerificationCode();

            Map<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("verification_code", verficationCode);
            map.put("verification_link", verificationLink);
            map.put("company_name", "DTSVN");

            Mail mail = new Mail();
            mail.setFrom(fromEmail);
            mail.setSubject(subject);

            Personalization personalization = new Personalization();
            personalization.addTo(toEmail);

            map.forEach(personalization::addDynamicTemplateData);

            mail.addPersonalization(personalization);
            mail.setTemplateId(templateId);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {
                log.info("Email verification sent successfully to {}", to);
            } else {
                log.error("Failed to send email verification. Status code: {}, Body: {}", response.getStatusCode(), response.getBody());
            }

            return verficationCode;
        } catch (IOException e) {
            log.error("Error sending email verification to {}: {}", to, e.getMessage());
            throw new RuntimeException("Error sending email verification", e);
        }

    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


    @Override
    public ResponseEntity<EmailResponse> getEmailById(int id) {
        com.minhductran.tutorial.minhductran.model.Email email = emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email not found with id: " + id));
        EmailResponse emailResponse = EmailResponse.builder()
                .id(email.getId())
                .text(email.getText())
                .subject(email.getSubject())
                .to(email.getTo())
                .build();
        return new ResponseEntity<>(emailResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<EmailResponse>> getAllEmails() {
        List<com.minhductran.tutorial.minhductran.model.Email> emails = emailRepository.findAll();
        List<EmailResponse> emailResponses = emails.stream()
                .map(email -> EmailResponse.builder()
                        .id(email.getId())
                        .text(email.getText())
                        .subject(email.getSubject())
                        .to(email.getTo())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(emailResponses, HttpStatus.OK);
    }
}
