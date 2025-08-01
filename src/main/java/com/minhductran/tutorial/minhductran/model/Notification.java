package com.minhductran.tutorial.minhductran.model;

import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.Map;

public class Notification extends AbstractEntity{
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "data")
    private Map<String,String> data;

    @Column(name = "sent_status", nullable = false)
    private boolean sentStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "is_seen")
    private boolean isSeen = false;

    // Các trường mới cho email
    @Column(name = "email_sent", nullable = false)
    private boolean emailSent = false;

    @Column(name = "email_recipient")
    private String emailRecipient;

    @Column(name = "email_error", columnDefinition = "TEXT")
    private String emailError;

    @Column(name = "email_sent_at")
    private LocalDateTime emailSentAt;
}
