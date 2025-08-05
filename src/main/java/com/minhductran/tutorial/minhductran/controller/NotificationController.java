package com.minhductran.tutorial.minhductran.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.minhductran.tutorial.minhductran.dto.request.Notification.*;
import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import com.minhductran.tutorial.minhductran.dto.response.NotificationResponse;
import com.minhductran.tutorial.minhductran.dto.response.NotificationSearchResponse;
import com.minhductran.tutorial.minhductran.model.Notification;
import com.minhductran.tutorial.minhductran.service.NotifcationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
@CrossOrigin
public class NotificationController {
    private final NotifcationService notificationService;

    //Gửi notification đến 1 thiết bị cụ thể
    @PostMapping("/send-to-device")
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody @Valid DeviceNotificationRequest request) {
        try {
            NotificationResponse response = notificationService.sendNotificationToDevice(request);
            return ResponseEntity.ok(response);
        } catch (FirebaseMessagingException | ExecutionException | InterruptedException e) {
            NotificationResponse errorResponse = NotificationResponse.builder()
                    .title("Failed to send notification.")
                    .message(e.getMessage()) // Include the exception message in the response
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/send-to-device-with-email")
    public ResponseEntity<NotificationResponse> sendNotificationWithEmail(@RequestBody @Valid DeviceNotificationRequest request,
                                                                          @RequestParam String email
    ) {
        try {
            NotificationResponse response = notificationService.sendNotificationWithEmail(request, email);
            return ResponseEntity.ok(response);
        } catch (FirebaseMessagingException | ExecutionException | InterruptedException e) {
            NotificationResponse errorResponse = NotificationResponse.builder()
                    .title("Failed to send notification with email.")
                    .message(e.getMessage()) // Include the exception message in the response
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    //Gửi notification đến 1 topic cụ thể khi đã subscribe
    @PostMapping("/send-to-topic")
    public ResponseEntity<String> sendNotificationToTopic(@RequestBody @Valid TopicNotificationRequest request) {
        try {
            notificationService.sendNotificationToTopic(request);
            return ResponseEntity.ok("Notification sent successfully.");
        } catch (FirebaseMessagingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification.");
        }
    }

    //Gửi notification đến tất cả các thiết bị
    @PostMapping("/send-to-all")
    public ResponseEntity<String> sendNotificationToAll(@RequestBody @Valid AllDevicesNotificationRequest request) {
        try {
            notificationService.sendNotificationToAll(request);
            return ResponseEntity.ok("Multicast notification sent successfully.");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send multicast notification.");
        }
    }

    // Subcribe đến 1 topic cụ thể
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToTopic(@RequestBody @Valid NotificationSubscriptionRequest request) {
        try {
            notificationService.subscribeToTopic(request);
            return ResponseEntity.ok("Device subscribed to the topic successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to subscribe device to the topic.");
        }
    }

    //Unsubcribe 1 topic cụ thể
    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeFromTopic(@RequestBody @Valid NotificationSubscriptionRequest request) {
        try {
            notificationService.unsubscribeFromTopic(request);
            return ResponseEntity.ok("Device unsubscribed from the topic successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unsubscribe device from the topic.");
        }
    }

    //Tìm kiếm notifications theo nhiều tiêu chí khác nhau
    @PostMapping("/search-notification")
    public ResponseEntity<ApiResponse<Page<NotificationSearchResponse>>> searchNotification(@RequestBody @Valid NotificationSearchRequest request) {
        try {
            Page<NotificationSearchResponse> result = notificationService.searchNotifications(request);
            ApiResponse<Page<NotificationSearchResponse>> response = new ApiResponse<>(
                    200, "Search completed successfully", result
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to search notifications: " + e.getMessage(), null));
        }
    }

    //Endpoint chính để lấy notifications cho device (bao gồm cả direct và topic)
    @GetMapping("/device/{deviceToken}")
    public ResponseEntity<ApiResponse<Page<Notification>>> getNotificationsByDevice(
            @PathVariable String deviceToken,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Boolean isSeen) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Notification> result = notificationService.getNotificationsByDevice(deviceToken, isSeen, pageable);
            ApiResponse<Page<Notification>> response = new ApiResponse<>(
                    200, "Device notifications retrieved successfully", result
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving device notifications: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to retrieve device notifications: " + e.getMessage(), null));
        }
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<Notification>>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Boolean isSeen) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
    
            Page<Notification> result = notificationService.getNotificationsBySeen(isSeen, pageable);
            ApiResponse<Page<Notification>> response = new ApiResponse<>(
                    200, "Notifications retrieved successfully", result
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving notifications: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to retrieve notifications: " + e.getMessage(), null));
        }
    }

    @PutMapping("/seen/{id}")
    public ResponseEntity<ApiResponse<String>> markNotificationAsSeen(@PathVariable int id) {
        try {
            ApiResponse<String> result = notificationService.markNotificationAsSeen(id);

            if (result.getStatus() == 200) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (Exception e) {
            log.error("Error marking notification as seen: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to mark notification as seen: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteNotification(@PathVariable int id) {
        try {
            ApiResponse<String> result = notificationService.deleteNotification(id);

            if (result.getStatus() == 200) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (Exception e) {
            log.error("Error deleting notification: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to delete notification: " + e.getMessage(), null));
        }
    }


    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteMultipleNotifications(
            @RequestBody @Valid DeleteNotificationRequest request) {
        try {
            ApiResponse<String> result = notificationService.deleteMultipleNotification(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error deleting multiple notifications: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to delete notifications: " + e.getMessage(), null));
        }
    }


}
