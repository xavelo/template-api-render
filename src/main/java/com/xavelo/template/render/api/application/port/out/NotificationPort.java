package com.xavelo.template.render.api.application.port.out;

import com.xavelo.template.render.api.domain.Notification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface NotificationPort {
    void createNotification(Notification notification);
    List<Notification> listNotifications(UUID authorizationId);
    void markNotificationSent(UUID notificationId, Instant sentAt);
    void respondToNotification(UUID notificationId, String status, Instant respondedAt, String respondedBy);
}
