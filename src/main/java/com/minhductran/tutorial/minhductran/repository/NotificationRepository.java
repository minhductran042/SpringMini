package com.minhductran.tutorial.minhductran.repository;

import com.minhductran.tutorial.minhductran.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // Additional query methods can be defined here if needed
    // For example, to find notifications by user ID or status
    // List<Notifications> findByUserId(Long userId);
    // List<Notifications> findByStatus(String status);

    Page<Notification> findByIsSeen(boolean isSeen, Pageable pageable); // Lấy tất cả notifications theo trạng thái isSeen

    @Query("SELECT n FROM Notification n WHERE " +
            "(:keyword IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.body) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:isSeen IS NULL OR n.isSeen = :isSeen) AND " +
            "(:startDate IS NULL OR n.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR n.createdAt <= :endDate) AND " +
            "(:deviceToken IS NULL OR n.deviceToken = :deviceToken)"
    )
    Page<Notification> searchNotifications( // Query để tìm kiếm notifications với các filter
            @Param("keyword") String keyword,
            @Param("isSeen") Boolean isSeen,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("deviceToken") String deviceToken,
            Pageable pageable
    );

    Page<Notification> findByDeviceToken(String deviceToken, Pageable pageable); // Lấy tất cả notifications theo device token

    Page<Notification> findByDeviceTokenAndIsSeen(String deviceToken, boolean isSeen, Pageable pageable); // Lấy notifications theo device token và trạng thái isSeen

    // Query chính để lấy notifications cho device (bao gồm cả direct và topic)
    @Query("SELECT DISTINCT n FROM Notification n " +
            "LEFT JOIN DeviceSubscription ds ON n.topicName = ds.topicName " +
            "WHERE (n.deviceToken = :deviceToken) OR " +
            "(n.topicName IS NOT NULL AND ds.deviceToken = :deviceToken AND ds.isActive = true) " +
            "ORDER BY n.createdAt DESC")
    Page<Notification> findNotificationsForDevice(@Param("deviceToken") String deviceToken, Pageable pageable); // Lấy tất cả notifications cho device, bao gồm cả những notifications từ topic mà device đã subscribe

    // Query với filter isSeen
    @Query("SELECT DISTINCT n FROM Notification n " +
            "LEFT JOIN DeviceSubscription ds ON n.topicName = ds.topicName " +
            "WHERE ((n.deviceToken = :deviceToken) OR " +
            "(n.topicName IS NOT NULL AND ds.deviceToken = :deviceToken AND ds.isActive = true)) " +
            "AND (:isSeen IS NULL OR n.isSeen = :isSeen) " +
            "ORDER BY n.createdAt DESC")
    Page<Notification> findNotificationsForDeviceWithSeenFilter( // Query để lấy notifications cho device với filter isSeen
            @Param("deviceToken") String deviceToken,
            @Param("isSeen") Boolean isSeen,
            Pageable pageable);

    // Query để lấy notifications theo topic cụ thể
    @Query("SELECT n FROM Notification n WHERE n.topicName = :topicName")
    Page<Notification> findByTopicName(@Param("topicName") String topicName, Pageable pageable); // Lấy tất cả notifications theo topic name

    // Query để lấy tổng số notifications chưa đọc của device
    @Query("SELECT COUNT(DISTINCT n) FROM Notification n " +
            "LEFT JOIN DeviceSubscription ds ON n.topicName = ds.topicName " +
            "WHERE ((n.deviceToken = :deviceToken) OR " +
            "(n.topicName IS NOT NULL AND ds.deviceToken = :deviceToken AND ds.isActive = true)) " +
            "AND n.isSeen = false")
    long countUnseenNotificationsForDevice(@Param("deviceToken") String deviceToken); // Lấy tổng số notifications chưa đọc của device

    // Query để mark tất cả notifications của device là seen
    @Modifying
    @Query("UPDATE Notification n SET n.isSeen = true WHERE n.id IN (" +
            "SELECT DISTINCT n2.id FROM Notification n2 " +
            "LEFT JOIN DeviceSubscription ds ON n2.topicName = ds.topicName " +
            "WHERE (n2.deviceToken = :deviceToken) OR " +
            "(n2.topicName IS NOT NULL AND ds.deviceToken = :deviceToken AND ds.isActive = true))")
    int markAllNotificationsAsSeenForDevice(@Param("deviceToken") String deviceToken); // Đánh dấu tất cả notifications của device là đã xem

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
    int deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate); // Xóa tất cả notifications cũ hơn một ngày

    Page<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable); // Lấy tất cả notifications trong khoảng thời gian nhất định
}
