package com.minhductran.tutorial.minhductran.dto.request.Notification;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TopicNotificationRequest extends NotificationRequest{
    @NotBlank
    private String topicName;
}
