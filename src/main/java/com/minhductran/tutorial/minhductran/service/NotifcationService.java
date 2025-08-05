package com.minhductran.tutorial.minhductran.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.minhductran.tutorial.minhductran.dto.request.Notification.*;
import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import com.minhductran.tutorial.minhductran.dto.response.NotificationResponse;
import com.minhductran.tutorial.minhductran.dto.response.NotificationSearchResponse;
import com.minhductran.tutorial.minhductran.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface NotifcationService {
    public NotificationResponse sendNotificationWithEmail(DeviceNotificationRequest request, String email) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    public NotificationResponse sendNotificationToDevice(DeviceNotificationRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    public void sendNotificationToTopic(TopicNotificationRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    public void sendNotificationToAll(AllDevicesNotificationRequest request) throws FirebaseMessagingException;
    public void subscribeToTopic(NotificationSubscriptionRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    public void unsubscribeFromTopic(NotificationSubscriptionRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    public Page<Notification> getNotificationsBySeen(Boolean isSeen, Pageable pageable);
    public Page<Notification> getNotificationsByDevice(String deviceToken, Boolean isSeen, Pageable pageable);
    public List<String> getDeviceSubscribedTopics(String deviceToken);
    public List<String> getTopicSubscribedDevices(String topicName);
    public Page<Notification> getDirectNotificationsByDevice(String deviceToken, Boolean isSeen, Pageable pageable);
    public Page<NotificationSearchResponse> searchNotifications(NotificationSearchRequest request);
    public ApiResponse<String> markNotificationAsSeen(int notificationId) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    public ApiResponse<String> deleteNotification(int notificationId);
    public ApiResponse<String> deleteMultipleNotification(DeleteNotificationRequest request);
}
