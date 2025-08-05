package com.minhductran.tutorial.minhductran.dto.request.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarkAsSeenRequest {
    private long notificationId;
    private boolean marked = false;
}
