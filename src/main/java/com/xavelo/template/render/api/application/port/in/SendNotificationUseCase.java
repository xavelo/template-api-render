package com.xavelo.template.render.api.application.port.in;

import java.util.UUID;

public interface SendNotificationUseCase {
    void sendNotification(UUID notificationId, UUID sentBy);
}
