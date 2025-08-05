package com.minhductran.tutorial.minhductran.dto.request.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteNotificationRequest {
    private List<Long> notificationIds; // Danh sách ID notification cần xóa
    private String deviceToken; // Token của thiết bị để xóa notification trên thiết bị đó
    private boolean deleteAll = false; // Xóa tất cả notification của user
    private Integer olderThanDays; // Xóa notification cũ hơn số ngày nhất định
}
