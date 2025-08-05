package com.minhductran.tutorial.minhductran.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import com.minhductran.tutorial.minhductran.dto.request.Notification.*;
import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import com.minhductran.tutorial.minhductran.dto.response.EmailResponse;
import com.minhductran.tutorial.minhductran.dto.response.NotificationResponse;
import com.minhductran.tutorial.minhductran.dto.response.NotificationSearchResponse;
import com.minhductran.tutorial.minhductran.model.DeviceSubscription;
import com.minhductran.tutorial.minhductran.model.Notification;
import com.minhductran.tutorial.minhductran.repository.DeviceSubcriptionRepository;
import com.minhductran.tutorial.minhductran.repository.EmailRepository;
import com.minhductran.tutorial.minhductran.repository.NotificationRepository;
import com.minhductran.tutorial.minhductran.service.EmailService;
import com.minhductran.tutorial.minhductran.service.NotifcationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
@Slf4j(topic = "Notification-Service")
public class NotificationServiceImpl implements NotifcationService {

    private final FirebaseApp firebaseApp;
    private final NotificationRepository notificationRepository;
    private final EmailRepository emailRepository;
    private final EmailService emailService;
    private final DeviceSubcriptionRepository deviceSubcriptionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NotificationResponse sendNotificationWithEmail(DeviceNotificationRequest request, String email) throws InterruptedException, ExecutionException  {
        // 1. Gửi Firebase notification
        Notification notification = createNotification(
                request.getDeviceToken(),
                null,
                request.getTitle(),
                request.getBody(),
                request.getImageUrl(),
                request.getData()
        );
        notificationRepository.save(notification);

        Message fcmMessage = Message.builder()
                .setToken(request.getDeviceToken())
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getBody())
                                .setImage(request.getImageUrl())
                                .build()
                )
                .putAllData(request.getData())
                .build();

        String response = FirebaseMessaging.getInstance(firebaseApp).sendAsync(fcmMessage).get();
        log.info("sendNotificationToDevice response: {}", response);

        notification.setSentStatus(true);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);

        // 2. Gửi email nếu có email - sử dụng EmailService hiện có
        if (email != null && !email.isEmpty()) {
            try {
                String emailSubject = request.getTitle();
                String text = request.getText();

                // Sử dụng method send của EmailService
                ResponseEntity<EmailResponse> emailResponse = emailService.sendEmail(email, emailSubject, text);

                if (emailResponse.getStatusCode().is2xxSuccessful()) {
                    log.info("Email sent successfully to: {}", email);
                    notification.setEmailSent(true);
                    notification.setEmailRecipient(email);
                    notification.setEmailSentAt(LocalDateTime.now());
                } else {
                    log.error("Failed to send email to: {}", email);
                    notification.setEmailSent(false);
                    notification.setEmailError("HTTP Status: " + emailResponse.getStatusCode());
                }

                notificationRepository.save(notification);

            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", email, e.getMessage());
                notification.setEmailSent(false);
                notification.setEmailError(e.getMessage());
                notificationRepository.save(notification);
            }
        }

        NotificationResponse notificationResponse = NotificationResponse.builder()
                .title(notification.getTitle())
                .body(notification.getBody())
                .tokenDevice(notification.getDeviceToken())
                .imageUrl(notification.getImageUrl())
                .sentAt(notification.getSentAt())
                .data(notification.getData())
                .isSeen(notification.isSeen())
                .emailSent(notification.isEmailSent())
                .emailRecipient(notification.getEmailRecipient())
                .emailSentAt(notification.getEmailSentAt())
                .build();

        return notificationResponse;
    }

    @Override
    public NotificationResponse sendNotificationToDevice(DeviceNotificationRequest request) throws InterruptedException, ExecutionException {
        return sendNotificationWithEmail(request,null);
    }

    @Override
    public void sendNotificationToTopic(TopicNotificationRequest request) throws  ExecutionException, InterruptedException{
        Notification notification = createNotification(null, request.getTopicName(), request.getTitle(), request.getBody(), request.getImageUrl(), request.getData());
        notificationRepository.save(notification);

        Message fcmMessage = Message.builder()
                .setTopic(request.getTopicName())
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getBody())
                                .setImage(request.getImageUrl())
                                .build()
                )
//                .setAndroidConfig(getAndroidConfig(request.getTopicName()))
                .setApnsConfig(getApnsConfig(request.getTopicName()))
                .putAllData(request.getData())
                .build();

        String response = FirebaseMessaging.getInstance(firebaseApp).sendAsync(fcmMessage).get();
        log.info("sendNotificationToDevice response: {}", response);

        notification.setSentStatus(true);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    @Override
    public void sendNotificationToAll(AllDevicesNotificationRequest request) throws FirebaseMessagingException{
        List<Notification> notifications = new ArrayList<>();
        for (String token : request.getDeviceTokenList().isEmpty() ? getAllDeviceTokens() : request.getDeviceTokenList()) {
            Notification notification = createNotification(token, null, request.getTitle(), request.getBody(), request.getImageUrl(), request.getData());
            notifications.add(notification);
        }
        notificationRepository.saveAll(notifications);


        MulticastMessage multicastMessage = MulticastMessage.builder()
                .addAllTokens(request.getDeviceTokenList().isEmpty() ? getAllDeviceTokens() : request.getDeviceTokenList())
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getBody())
                                .setImage(request.getImageUrl())
                                .build()
                )
                .putAllData(request.getData())
                .build();

        // Process the response
        BatchResponse response = FirebaseMessaging.getInstance(firebaseApp).sendEachForMulticast(multicastMessage);
        for (int i = 0; i < response.getResponses().size(); i++) {
            SendResponse sendResponse = response.getResponses().get(i);
            Notification notif = notifications.get(i);
            if (sendResponse.isSuccessful()) {
                notif.setSentStatus(true);
                notif.setSentAt(LocalDateTime.now());
                log.info("Message sent successfully to: {}", sendResponse.getMessageId());
            } else {
                notif.setSentStatus(false);
                log.error("Failed to send message to: {}", sendResponse.getMessageId());
                log.error("Error details: {}", sendResponse.getException().getMessage());
            }
            notificationRepository.save(notif);
        }
    }

    private List<String> getAllDeviceTokens() {
        // Implement logic to retrieve all device tokens from your database or storage
        // Return a list of device tokens
        return new ArrayList<>();
    }

    @Override
    public void subscribeToTopic(NotificationSubscriptionRequest request) throws FirebaseMessagingException {
        FirebaseMessaging.getInstance().subscribeToTopic(
                Collections.singletonList(request.getDeviceToken()),
                request.getTopicName()
        );
        // Lưu subscription vào database nếu cần
        DeviceSubscription deviceSubscription = new DeviceSubscription();
        deviceSubscription.setDeviceToken(request.getDeviceToken());
        deviceSubscription.setTopicName(request.getTopicName());
        deviceSubscription.setActive(true);
        deviceSubcriptionRepository.save(deviceSubscription);
        log.info("Device {} subscribed to topic {}", request.getDeviceToken(), request.getTopicName());
    }

    @Override
    public void unsubscribeFromTopic(NotificationSubscriptionRequest request) throws FirebaseMessagingException{
        FirebaseMessaging.getInstance().unsubscribeFromTopic(
                Collections.singletonList(request.getDeviceToken()),
                request.getTopicName()
        );
        deviceSubcriptionRepository.deactivateSubscription(request.getDeviceToken(), request.getTopicName());
        log.info("Device {} unsubscribed from topic {}", request.getDeviceToken(), request.getTopicName());
    }

    @Override
    public Page<Notification> getNotificationsBySeen(Boolean isSeen, Pageable pageable) {
        if (isSeen != null) {
            return notificationRepository.findByIsSeen(isSeen, pageable);
        }
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Page<Notification> getNotificationsByDevice(String deviceToken, Boolean isSeen, Pageable pageable) {
        if (isSeen != null) {
            return notificationRepository.findByDeviceTokenAndIsSeen(deviceToken, isSeen, pageable);
        }
        return notificationRepository.findByDeviceToken(deviceToken,pageable);
    }

    // Lấy danh sách topics mà device đã subscribe
    @Override
    public List<String> getDeviceSubscribedTopics(String deviceToken) {
        return deviceSubcriptionRepository.findTopicsByDeviceToken(deviceToken);
    }

    // Lấy danh sách devices đã subscribe một topic
    @Override
    public List<String> getTopicSubscribedDevices(String topicName) {
        return deviceSubcriptionRepository.findDeviceTokensByTopic(topicName);
    }

    @Override
    public Page<Notification> getDirectNotificationsByDevice(String deviceToken, Boolean isSeen, Pageable pageable) {
        if (isSeen != null) {
            return notificationRepository.findByDeviceTokenAndIsSeen(deviceToken, isSeen, pageable);
        }
        return notificationRepository.findByDeviceToken(deviceToken, pageable);
    }

    @Override
    public Page<NotificationSearchResponse> searchNotifications(NotificationSearchRequest request) {
        try {
            // Tạo Sort object
            Sort.Direction direction = "desc".equalsIgnoreCase(request.getSortDirection()) ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, request.getSortBy());

            // Tạo Pageable object sử dụng PageRequest.of()
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

            // Gọi repository để tìm kiếm
            Page<Notification> notifications = notificationRepository.searchNotifications(
                    request.getKeyword(),
                    request.getIsSeen(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getDeviceToken(),
                    pageable
            );

            // Convert sang DTO
            return notifications.map(this::convertToSearchResponse);

        } catch (Exception e) {
            log.error("Error searching notifications: ", e);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    private Notification createNotification(String deviceToken, String topicName, String title, String body, String imageUrl, Map<String, String> data) {
        Notification notification = new Notification();
        notification.setDeviceToken(deviceToken);
        notification.setTopicName(topicName);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setImageUrl(imageUrl);
        notification.setData(data);
        notification.setSentStatus(false);
        return notification;
    }


    @Override
    public ApiResponse<String> deleteNotification(int notificationId) {
        try {
            if (!notificationRepository.existsById(notificationId)) {
                return new ApiResponse<>(404, "Notification not found", null);
            }

            notificationRepository.deleteById(notificationId);
            return new ApiResponse<>(200, "Notification deleted successfully", null);

        } catch (Exception e) {
            log.error("Error deleting notification: ", e);
            return new ApiResponse<>(500, "Failed to delete notification", null);
        }
    }

    @Override
    public ApiResponse<String> deleteMultipleNotification(DeleteNotificationRequest request) {
        try {
            int deletedCount = 0;

            if (request.getOlderThanDays() != null && request.getOlderThanDays() > 0) {
                // Xóa notification cũ hơn số ngày nhất định
                LocalDateTime cutoffDate = LocalDateTime.now().minusDays(request.getOlderThanDays());
                deletedCount = notificationRepository.deleteOldNotifications(cutoffDate);
            } else if (request.getNotificationIds() != null && !request.getNotificationIds().isEmpty()) {
                // Xóa theo danh sách ID
                List<Integer> notificationIds = request.getNotificationIds().stream()
                        .map(Long::intValue)
                        .toList();
                List<Notification> notifications = notificationRepository.findAllById(notificationIds);
                notificationRepository.deleteAll(notifications);
                deletedCount = notifications.size();
            }

            return new ApiResponse<>(200, "Successfully deleted " + deletedCount + " notifications", null);

        } catch (Exception e) {
            log.error("Error deleting multiple notifications: ", e);
            return new ApiResponse<>(500, "Failed to delete notifications", null);
        }
    }

    //Đánh dấu notification là đã xem
    @Override
    @Transactional
    public ApiResponse<String> markNotificationAsSeen(int notificationId) {
        try {
            Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
            if (optionalNotification.isPresent()) {
                Notification notification = optionalNotification.get();
                notification.setSeen(true);
                notificationRepository.save(notification);
                return new ApiResponse<>(200, "Notification marked as seen successfully", null);
            } else {
                return new ApiResponse<>(404, "Notification not found", null);
            }
        } catch (Exception e) {
            log.error("Error marking notification as seen: ", e);
            return new ApiResponse<>(500, "Error marking notification as seen: " + e.getMessage(), null);
        }
    }

    private NotificationSearchResponse convertToSearchResponse(Notification notification) {
        NotificationSearchResponse response = new NotificationSearchResponse();
        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setBody(notification.getBody());
        response.setDeviceToken(notification.getDeviceToken());
        response.setTopicName(notification.getTopicName());
        response.setImageUrl(notification.getImageUrl());
        response.setSentStatus(notification.isSentStatus());
        response.setSentAt(notification.getSentAt());
        response.setSeen(notification.isSeen());
        return response;
    }

    // Helper method để tránh code duplicate
    private NotificationResponse buildNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .title(notification.getTitle())
                .body(notification.getBody())
                .tokenDevice(notification.getDeviceToken())
                .imageUrl(notification.getImageUrl())
                .sentAt(notification.getSentAt())
                .data(notification.getData())
                .isSeen(notification.isSeen())
                .build();
    }
}
