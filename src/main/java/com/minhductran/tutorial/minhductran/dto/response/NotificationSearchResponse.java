package com.minhductran.tutorial.minhductran.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class NotificationSearchResponse {
    private int id;
    private String title;
    private String body;
    private String deviceToken;
    private String topicName;
    private String imageUrl;
    private String data;
    private boolean sentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private boolean isSeen;
}
