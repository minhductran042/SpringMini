package com.minhductran.tutorial.minhductran.dto.request.Notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
public class AllDevicesNotificationRequest extends NotificationRequest{
    List<String> deviceTokenList = new ArrayList<>();
}
