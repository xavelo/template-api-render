package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.SendNotificationsUseCase;
import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService implements SendNotificationsUseCase {

    private final NotificationPort notificationPort;
    private final NotificationEmailPort notificationEmailPort;

    public NotificationService(NotificationPort notificationPort, NotificationEmailPort notificationEmailPort) {
        this.notificationPort = notificationPort;
        this.notificationEmailPort = notificationEmailPort;
    }

    @Override
    public void sendNotifications(UUID authorizationId) {
        List<Notification> notifications = notificationPort.listNotifications(authorizationId);
        for (Notification notification : notifications) {
            if (notification.status() == NotificationStatus.PENDING) {
                notificationEmailPort.sendNotificationEmail(notification);
                notificationPort.markNotificationSent(notification.id(), Instant.now());
            }
        }
    }
}
