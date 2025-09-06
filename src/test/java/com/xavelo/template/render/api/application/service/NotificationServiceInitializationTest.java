package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.adapter.out.email.NoOpEmailAdapter;
import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

class NotificationServiceInitializationTest {

    @Test
    void notificationServiceStartsWithoutMailSender() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(NoOpEmailAdapter.class, NotificationService.class, TestConfig.class);
        context.refresh();
        context.getBean(NotificationService.class);
        context.close();
    }

    @Configuration
    static class TestConfig {
        @Bean
        NotificationPort notificationPort() {
            return new NotificationPort() {
                @Override
                public void createNotification(Notification notification) {
                }

                @Override
                public List<Notification> listNotifications(UUID authorizationId) {
                    return List.of();
                }

                @Override
                public void markNotificationSent(UUID notificationId, Instant sentAt) {
                }

                @Override
                public void respondToNotification(UUID notificationId, NotificationStatus status, Instant respondedAt, String respondedBy) {
                }

                @Override
                public List<Notification> listNotificationsByStatus(NotificationStatus status) {
                    return List.of();
                }

                @Override
                public void expireNotification(UUID notificationId) {
                }
            };
        }
    }
}
