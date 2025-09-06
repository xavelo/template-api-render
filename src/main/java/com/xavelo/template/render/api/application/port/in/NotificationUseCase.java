package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;

import java.util.List;
import java.util.UUID;

public interface NotificationUseCase {
    List<Notification> listNotifications(UUID authorizationId);
    void markNotificationSent(UUID notificationId);
    void respondToNotification(UUID notificationId, NotificationStatus status, String respondedBy);
}
