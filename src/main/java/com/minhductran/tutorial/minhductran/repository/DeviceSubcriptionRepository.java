package com.minhductran.tutorial.minhductran.repository;

import com.minhductran.tutorial.minhductran.model.DeviceSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceSubcriptionRepository extends JpaRepository<DeviceSubscription, Integer> {
    // Tìm subscription theo device token và topic
    Optional<DeviceSubscription> findByDeviceTokenAndTopicName(String deviceToken, String topicName);

    // Lấy tất cả topics mà device đã subscribe
    @Query("SELECT ds.topicName FROM DeviceSubscription ds WHERE ds.deviceToken = :deviceToken AND ds.isActive = true")
    List<String> findTopicsByDeviceToken(@Param("deviceToken") String deviceToken);

    // Lấy tất cả devices subscribe một topic
    @Query("SELECT ds.deviceToken FROM DeviceSubscription ds WHERE ds.topicName = :topicName AND ds.isActive = true")
    List<String> findDeviceTokensByTopic(@Param("topicName") String topicName);

    // Lấy tất cả subscriptions của một device
    List<DeviceSubscription> findByDeviceTokenAndIsActive(String deviceToken, boolean isActive);

    // Lấy tất cả subscriptions của một topic
    List<DeviceSubscription> findByTopicNameAndIsActive(String topicName, boolean isActive);

    // Deactivate subscription thay vì xóa
    @Modifying
    @Query("UPDATE DeviceSubscription ds SET ds.isActive = false WHERE ds.deviceToken = :deviceToken AND ds.topicName = :topicName")
    int deactivateSubscription(@Param("deviceToken") String deviceToken, @Param("topicName") String topicName);

    // Activate subscription
    @Modifying
    @Query("UPDATE DeviceSubscription ds SET ds.isActive = true WHERE ds.deviceToken = :deviceToken AND ds.topicName = :topicName")
    int activateSubscription(@Param("deviceToken") String deviceToken, @Param("topicName") String topicName);

}
