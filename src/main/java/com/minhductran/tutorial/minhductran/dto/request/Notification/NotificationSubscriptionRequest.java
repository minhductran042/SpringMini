package com.minhductran.tutorial.minhductran.dto.request.Notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;


@Getter
@Setter
@Builder
public class NotificationSubscriptionRequest  extends NotificationRequest{
    @NotBlank
    private String deviceToken;
    @NotBlank
    private String topicName;
}
