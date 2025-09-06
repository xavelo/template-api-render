package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.SendNotificationUseCase;
import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class NotificationService implements SendNotificationUseCase {

    private final NotificationPort notificationPort;
    private final NotificationEmailPort notificationEmailPort;

    public NotificationService(NotificationPort notificationPort, NotificationEmailPort notificationEmailPort) {
        this.notificationPort = notificationPort;
        this.notificationEmailPort = notificationEmailPort;
    }

    @Override
    public void sendNotification(UUID authorizationId, UUID studentId, UUID guardianId) {
        Notification notification = new Notification(
                UUID.randomUUID(),
                authorizationId,
                studentId,
                guardianId,
                NotificationStatus.SENT,
                Instant.now(),
                null,
                null
        );
        notificationEmailPort.sendNotificationEmail(notification);
        notificationPort.createNotification(notification);
    }
}
