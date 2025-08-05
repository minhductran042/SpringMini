package com.minhductran.tutorial.minhductran.dto.request.Notification;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeviceNotificationRequest extends NotificationRequest {
    @NotBlank
    private String deviceToken;

    @Nullable
    private String text; // Cho email text

}
