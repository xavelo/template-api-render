package com.xavelo.template.render.api.adapter.in.http.notification;

import com.xavelo.template.render.api.application.port.in.NotificationUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import com.xavelo.template.render.api.domain.Notification;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    public NotificationController(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> listNotifications() {
        List<Notification> notifications = notificationUseCase.listNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/notifications/authorization/{authorizationId}")
    public ResponseEntity<List<Notification>> listNotificationsByAuthorization(@PathVariable UUID authorizationId) {
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
