package com.xavelo.template.render.api.adapter.in.http.authorization;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.NotificationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.GetAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.ListAuthorizationsUseCase;
import com.xavelo.template.render.api.application.exception.UserNotFoundException;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.domain.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private static final Logger logger = LogManager.getLogger(AuthorizationController.class);

    private final CreateAuthorizationUseCase createAuthorizationUseCase;
    private final AssignStudentsToAuthorizationUseCase assignStudentsToAuthorizationUseCase;
    private final ListAuthorizationsUseCase listAuthorizationsUseCase;
    private final GetAuthorizationUseCase getAuthorizationUseCase;
    private final NotificationUseCase notificationUseCase;

    public AuthorizationController(CreateAuthorizationUseCase createAuthorizationUseCase,
                                   AssignStudentsToAuthorizationUseCase assignStudentsToAuthorizationUseCase,
                                   ListAuthorizationsUseCase listAuthorizationsUseCase,
                                   GetAuthorizationUseCase getAuthorizationUseCase,
                                   NotificationUseCase notificationUseCase) {
        this.createAuthorizationUseCase = createAuthorizationUseCase;
        this.assignStudentsToAuthorizationUseCase = assignStudentsToAuthorizationUseCase;
        this.listAuthorizationsUseCase = listAuthorizationsUseCase;
        this.getAuthorizationUseCase = getAuthorizationUseCase;
        this.notificationUseCase = notificationUseCase;
    }

    @PostMapping("/authorization")
    public ResponseEntity<Authorization> createAuthorization(@RequestBody CreateAuthorizationRequest request) {
        if (!StringUtils.hasText(request.title())
                || !StringUtils.hasText(request.text())
                || !StringUtils.hasText(request.status())
                || !StringUtils.hasText(request.createdBy())) {
            logger.warn("Missing required field when creating authorization");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            UUID.fromString(request.createdBy());
        } catch (IllegalArgumentException ex) {
            logger.warn("Invalid created_by when creating authorization");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Authorization authorization = createAuthorizationUseCase.createAuthorization(
                    request.title(),
                    request.text(),
                    request.status(),
                    request.createdBy(),
                    request.sentBy(),
                    request.approvedBy()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(authorization);
        } catch (UserNotFoundException ex) {
            logger.warn("created_by user does not exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/authorization/{authorizationId}/students")
    public ResponseEntity<Void> assignStudentsToAuthorization(@PathVariable UUID authorizationId,
                                                              @RequestBody AssignStudentsRequest request) {
        assignStudentsToAuthorizationUseCase.assignStudents(authorizationId, request.studentIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/authorizations")
    public ResponseEntity<List<Authorization>> listAuthorizations() {
        List<Authorization> authorizations = listAuthorizationsUseCase.listAuthorizations();
        return ResponseEntity.ok(authorizations);
    }

    @GetMapping("/authorization/{authorizationId}")
    public ResponseEntity<Authorization> getAuthorization(@PathVariable UUID authorizationId) {
        return getAuthorizationUseCase.getAuthorization(authorizationId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/authorization/{authorizationId}/notifications")
    public ResponseEntity<List<Notification>> listNotifications(@PathVariable UUID authorizationId) {
        List<Notification> notifications = notificationUseCase.listNotifications(authorizationId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/notification/{notificationId}/sent")
    public ResponseEntity<Void> markNotificationSent(@PathVariable UUID notificationId) {
        notificationUseCase.markNotificationSent(notificationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notification/{notificationId}/response")
    public ResponseEntity<Void> respondToNotification(@PathVariable UUID notificationId,
                                                      @RequestBody RespondNotificationRequest request) {
        notificationUseCase.respondToNotification(notificationId, request.status(), request.respondedBy());
        return ResponseEntity.ok().build();
    }
}
