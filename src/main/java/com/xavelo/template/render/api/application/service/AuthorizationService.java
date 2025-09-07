package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.NotificationUseCase;
import com.xavelo.template.render.api.application.port.in.SendNotificationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.GetAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.ListAuthorizationsUseCase;
import com.xavelo.template.render.api.application.port.out.AssignStudentsToAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.NotificationPort;
import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.port.out.ListAuthorizationsPort;
import com.xavelo.template.render.api.application.port.out.ListStudentsPort;
import com.xavelo.template.render.api.application.exception.UserNotFoundException;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import com.xavelo.template.render.api.domain.Student;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorizationService implements CreateAuthorizationUseCase, AssignStudentsToAuthorizationUseCase,
        ListAuthorizationsUseCase, GetAuthorizationUseCase, NotificationUseCase {

    private final CreateAuthorizationPort createAuthorizationPort;
    private final AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort;
    private final ListAuthorizationsPort listAuthorizationsPort;
    private final GetAuthorizationPort getAuthorizationPort;
    private final GetUserPort getUserPort;
    private final ListStudentsPort listStudentsPort;
    private final NotificationPort notificationPort;
    private final SendNotificationUseCase sendNotificationUseCase;

    public AuthorizationService(CreateAuthorizationPort createAuthorizationPort,
                                AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort,
                                ListAuthorizationsPort listAuthorizationsPort,
                                GetAuthorizationPort getAuthorizationPort,
                                GetUserPort getUserPort,
                                ListStudentsPort listStudentsPort,
                                NotificationPort notificationPort,
                                SendNotificationUseCase sendNotificationUseCase) {
        this.createAuthorizationPort = createAuthorizationPort;
        this.assignStudentsToAuthorizationPort = assignStudentsToAuthorizationPort;
        this.listAuthorizationsPort = listAuthorizationsPort;
        this.getAuthorizationPort = getAuthorizationPort;
        this.getUserPort = getUserPort;
        this.listStudentsPort = listStudentsPort;
        this.notificationPort = notificationPort;
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @Override
    public Authorization createAuthorization(String title, String text, String status, UUID createdBy, String sentBy,
                                             String approvedBy, Instant expiresAt) {
        if (getUserPort.getUser(createdBy).isEmpty()) {
            throw new UserNotFoundException(createdBy);
        }

        Authorization authorization = new Authorization(UUID.randomUUID(), title, text, status, null, createdBy, null, sentBy,
                null, approvedBy, expiresAt, List.of());
        return createAuthorizationPort.createAuthorization(authorization);
    }

    @Override
    public void assignStudents(UUID authorizationId, java.util.List<UUID> studentIds) {
        assignStudentsToAuthorizationPort.assignStudentsToAuthorization(authorizationId, studentIds);
        List<Student> students = listStudentsPort.listStudents();
        for (UUID studentId : studentIds) {
            students.stream().filter(s -> s.id().equals(studentId)).findFirst().ifPresent(student -> {
                if (student.guardianIds() != null) {
                    for (UUID guardianId : student.guardianIds()) {
                        sendNotificationUseCase.sendNotification(authorizationId, studentId, guardianId);
                    }
                }
            });
        }
    }

    @Override
    public List<Authorization> listAuthorizations() {
        return listAuthorizationsPort.listAuthorizations();
    }

    @Override
    public Optional<Authorization> getAuthorization(UUID authorizationId) {
        return getAuthorizationPort.getAuthorization(authorizationId);
    }

    @Override
    public List<Notification> listNotifications(UUID authorizationId) {
        return notificationPort.listNotifications(authorizationId);
    }

    @Override
    public void markNotificationSent(UUID notificationId) {
        notificationPort.markNotificationSent(notificationId, Instant.now());
    }

    @Override
    public void respondToNotification(UUID notificationId, NotificationStatus status, String respondedBy) {
        notificationPort.respondToNotification(notificationId, status, Instant.now(), respondedBy);
    }
}
