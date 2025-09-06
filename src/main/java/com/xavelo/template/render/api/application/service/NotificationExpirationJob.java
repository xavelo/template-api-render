package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.application.port.out.GetAuthorizationPort;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class NotificationExpirationJob {

    private final NotificationPort notificationPort;
    private final GetAuthorizationPort getAuthorizationPort;

    public NotificationExpirationJob(NotificationPort notificationPort, GetAuthorizationPort getAuthorizationPort) {
        this.notificationPort = notificationPort;
        this.getAuthorizationPort = getAuthorizationPort;
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void expireNotifications() {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        List<Notification> notifications = notificationPort.listNotificationsByStatus(NotificationStatus.SENT);
        for (Notification notification : notifications) {
            getAuthorizationPort.getAuthorization(notification.authorizationId()).ifPresent(authorization -> {
                LocalDate expiry = authorization.expiresAt().atZone(ZoneId.systemDefault()).toLocalDate();
                if (expiry.equals(today)) {
                    notificationPort.expireNotification(notification.id());
                }
            });
        }
    }
}
