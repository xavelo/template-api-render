package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.AssignStudentsToAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.port.out.ListAuthorizationsPort;
import com.xavelo.template.render.api.application.port.out.ListStudentsPort;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import com.xavelo.template.render.api.domain.Student;
import com.xavelo.template.render.api.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.xavelo.template.render.api.application.exception.UserNotFoundException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorizationServiceTest {

    @Mock
    private CreateAuthorizationPort createAuthorizationPort;
    @Mock
    private AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort;
    @Mock
    private ListAuthorizationsPort listAuthorizationsPort;
    @Mock
    private GetUserPort getUserPort;
    @Mock
    private GetAuthorizationPort getAuthorizationPort;
    @Mock
    private ListStudentsPort listStudentsPort;
    @Mock
    private NotificationPort notificationPort;

    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorizationService = new AuthorizationService(createAuthorizationPort, assignStudentsToAuthorizationPort,
                listAuthorizationsPort, getAuthorizationPort, getUserPort, listStudentsPort, notificationPort);
    }

    @Test
    void whenCreatedByUserDoesNotExist_thenThrowsConflict() {
        UUID createdBy = UUID.randomUUID();
        Mockito.when(getUserPort.getUser(createdBy)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                authorizationService.createAuthorization("Title", "Text", "draft", createdBy, null, null, Instant.now()));
    }

    @Test
    void whenCreatedByUserExists_thenCreatesAuthorization() {
        UUID createdBy = UUID.randomUUID();
        Mockito.when(getUserPort.getUser(createdBy)).thenReturn(Optional.of(new User(createdBy, "name")));
        Authorization authorization = new Authorization(UUID.randomUUID(), "Title", "Text", "draft", null, createdBy, null, null, null, null, Instant.now(), java.util.List.of(), java.util.List.of());
        Mockito.when(createAuthorizationPort.createAuthorization(Mockito.any())).thenReturn(authorization);

        Authorization result = authorizationService.createAuthorization("Title", "Text", "draft", createdBy, null, null, Instant.now());
        assertEquals(authorization, result);
    }

    @Test
    void whenAssigningStudents_thenCreatesNotificationsForGuardians() {
        UUID authorizationId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID guardianId = UUID.randomUUID();
        Student student = new Student(studentId, "name", java.util.List.of(guardianId));
        Mockito.when(listStudentsPort.listStudents()).thenReturn(java.util.List.of(student));

        authorizationService.assignStudents(authorizationId, java.util.List.of(studentId));

        Mockito.verify(notificationPort).createNotification(Mockito.argThat(n ->
                n.authorizationId().equals(authorizationId) &&
                        n.studentId().equals(studentId) &&
                        n.guardianId().equals(guardianId) &&
                        n.status() == NotificationStatus.PENDING));
    }

    @Test
    void whenMarkingNotificationSent_thenCallsPort() {
        UUID notificationId = UUID.randomUUID();
        authorizationService.markNotificationSent(notificationId);
        Mockito.verify(notificationPort).markNotificationSent(Mockito.eq(notificationId), Mockito.any());
    }

    @Test
    void whenRespondingToNotification_thenCallsPort() {
        UUID notificationId = UUID.randomUUID();
        authorizationService.respondToNotification(notificationId, NotificationStatus.APPROVED, "guardian");
        Mockito.verify(notificationPort).respondToNotification(Mockito.eq(notificationId), Mockito.eq(NotificationStatus.APPROVED), Mockito.any(), Mockito.eq("guardian"));
    }
}
