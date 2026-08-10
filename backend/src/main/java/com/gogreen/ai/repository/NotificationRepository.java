package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Notification;
import com.gogreen.ai.entity.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByUserId(UUID userId);

    List<Notification> findByUserIdAndReadFalse(UUID userId);

    List<Notification> findByUserIdAndType(UUID userId, NotificationType type);
}
