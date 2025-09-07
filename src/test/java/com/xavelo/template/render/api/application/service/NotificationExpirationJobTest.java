package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.GetAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class NotificationExpirationJobTest {

    @Mock
    private NotificationPort notificationPort;
    @Mock
    private GetAuthorizationPort getAuthorizationPort;

    private NotificationExpirationJob job;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        job = new NotificationExpirationJob(notificationPort, getAuthorizationPort);
    }

    @Test
    void expireNotifications_shouldUpdateStatusWhenAuthorizationExpiresToday() {
        UUID authorizationId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        Notification notification = new Notification(notificationId, authorizationId, UUID.randomUUID(), UUID.randomUUID(),
                NotificationStatus.SENT, Instant.now(), null, null);
        Mockito.when(notificationPort.listNotificationsByStatus(NotificationStatus.SENT))
                .thenReturn(List.of(notification));
        Authorization authorization = new Authorization(authorizationId, "", "", "", null, null, null, "", null, "",
                Instant.now(), List.of(), List.of());
        Mockito.when(getAuthorizationPort.getAuthorization(authorizationId)).thenReturn(Optional.of(authorization));

        job.expireNotifications();

        Mockito.verify(notificationPort).expireNotification(notificationId);
    }
}
