package com.xavelo.template.render.api.adapter.in.http.notification;

import com.xavelo.template.render.api.application.port.in.ListNotificationsUseCase;
import com.xavelo.template.render.api.application.port.in.ListNotificationsByAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.SendNotificationUseCase;
import com.xavelo.template.render.api.application.port.in.RespondNotificationUseCase;
import com.xavelo.template.render.api.domain.Notification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final ListNotificationsUseCase listNotificationsUseCase;
    private final ListNotificationsByAuthorizationUseCase listNotificationsByAuthorizationUseCase;
    private final SendNotificationUseCase sendNotificationUseCase;
    private final RespondNotificationUseCase respondNotificationUseCase;

    public NotificationController(ListNotificationsUseCase listNotificationsUseCase,
                                  ListNotificationsByAuthorizationUseCase listNotificationsByAuthorizationUseCase,
                                  SendNotificationUseCase sendNotificationUseCase,
                                  RespondNotificationUseCase respondNotificationUseCase) {
        this.listNotificationsUseCase = listNotificationsUseCase;
        this.listNotificationsByAuthorizationUseCase = listNotificationsByAuthorizationUseCase;
        this.sendNotificationUseCase = sendNotificationUseCase;
        this.respondNotificationUseCase = respondNotificationUseCase;
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> listNotifications() {
        List<Notification> notifications = listNotificationsUseCase.listNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/notifications/authorization/{authorizationId}")
    public ResponseEntity<List<Notification>> listNotificationsByAuthorization(@PathVariable UUID authorizationId) {
        List<Notification> notifications = listNotificationsByAuthorizationUseCase.listNotifications(authorizationId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/notification/{notificationId}/sent")
    public ResponseEntity<Void> sendNotification(@PathVariable UUID notificationId,
                                                 @RequestParam UUID sentBy) {
        sendNotificationUseCase.sendNotification(notificationId, sentBy);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notification/{notificationId}/response")
    public ResponseEntity<Void> respondToNotification(@PathVariable UUID notificationId,
                                                      @RequestBody RespondNotificationRequest request) {
        respondNotificationUseCase.respondToNotification(notificationId, request.status(), request.respondedBy());
        return ResponseEntity.ok().build();
    }
}
