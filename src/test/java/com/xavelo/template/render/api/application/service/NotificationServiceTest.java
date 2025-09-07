package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

class NotificationServiceTest {

    @Mock
    private NotificationPort notificationPort;
    @Mock
    private NotificationEmailPort notificationEmailPort;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationService(notificationPort, notificationEmailPort);
    }

    @Test
    void whenSendingNotifications_thenSendsPendingOnes() {
        UUID authorizationId = UUID.randomUUID();
        Notification pending = new Notification(UUID.randomUUID(), authorizationId,
                UUID.randomUUID(), UUID.randomUUID(), NotificationStatus.PENDING, null, null, null);
        Notification sent = new Notification(UUID.randomUUID(), authorizationId,
                UUID.randomUUID(), UUID.randomUUID(), NotificationStatus.SENT, Instant.now(), null, null);
        Mockito.when(notificationPort.listNotifications(authorizationId)).thenReturn(List.of(pending, sent));

        notificationService.sendNotifications(authorizationId);

        Mockito.verify(notificationEmailPort).sendNotificationEmail(pending);
        Mockito.verify(notificationPort).markNotificationSent(Mockito.eq(pending.id()), Mockito.any());
        Mockito.verify(notificationEmailPort, Mockito.never()).sendNotificationEmail(sent);
    }
}
