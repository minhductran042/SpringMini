package com.minhductran.tutorial.minhductran.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_subscriptions")
@Getter
@Setter
public class DeviceSubscription extends AbstractEntity {
    @Column(name = "device_token", nullable = false, length = 500)
    private String deviceToken;

    @Column(name = "topic_name", nullable = false, length = 255)
    private String topicName;

    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        subscribedAt = LocalDateTime.now();
    }
}
