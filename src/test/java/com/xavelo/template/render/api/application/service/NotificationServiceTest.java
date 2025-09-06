package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    void whenSendingNotification_thenPersistsAndSendsEmail() {
        UUID authorizationId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID guardianId = UUID.randomUUID();

        notificationService.sendNotification(authorizationId, studentId, guardianId);

        Mockito.verify(notificationPort).createNotification(ArgumentMatchers.argThat(n ->
                n.authorizationId().equals(authorizationId) &&
                        n.studentId().equals(studentId) &&
                        n.guardianId().equals(guardianId) &&
                        n.status().equals("SENT")));
        Mockito.verify(notificationEmailPort).sendNotificationEmail(ArgumentMatchers.any(Notification.class));
    }
}
