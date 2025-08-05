package com.minhductran.tutorial.minhductran.dto.request.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSearchRequest {
    private String keyword; // Tìm kiếm trong title và body
    private Boolean isSeen; // Lọc theo trạng thái đã đọc
    private LocalDateTime startDate; // Lọc từ ngày
    private LocalDateTime endDate; // Lọc đến ngày
    private String deviceToken; // Lọc theo device token

    // Pagination
    private int page = 0;
    private int size = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
}
