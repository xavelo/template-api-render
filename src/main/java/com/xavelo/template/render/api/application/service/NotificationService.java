package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.ListNotificationsUseCase;
import com.xavelo.template.render.api.application.port.in.ListNotificationsByAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.SendNotificationUseCase;
import com.xavelo.template.render.api.application.port.in.RespondNotificationUseCase;
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
public class NotificationService implements SendNotificationsUseCase,
        ListNotificationsUseCase,
        ListNotificationsByAuthorizationUseCase,
        SendNotificationUseCase,
        RespondNotificationUseCase {

    private final NotificationPort notificationPort;
    private final NotificationEmailPort notificationEmailPort;

    public NotificationService(NotificationPort notificationPort,
                               NotificationEmailPort notificationEmailPort) {
        this.notificationPort = notificationPort;
        this.notificationEmailPort = notificationEmailPort;
    }

    @Override
    public List<Notification> listNotifications() {
        return notificationPort.listNotifications();
    }

    @Override
    public List<Notification> listNotifications(UUID authorizationId) {
        return notificationPort.listNotifications(authorizationId);
    }

    @Override
    public void sendNotification(UUID notificationId, UUID sentBy) {
        notificationPort.getNotification(notificationId).ifPresent(notification -> {
            notificationEmailPort.sendNotificationEmail(notification);
            notificationPort.markNotificationSent(notificationId, Instant.now());
        });
    }

    @Override
    public void respondToNotification(UUID notificationId, NotificationStatus status, String respondedBy) {
        notificationPort.respondToNotification(notificationId, status, Instant.now(), respondedBy);
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
