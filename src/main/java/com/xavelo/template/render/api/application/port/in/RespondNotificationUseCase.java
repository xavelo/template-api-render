package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.NotificationStatus;
import java.util.UUID;

public interface RespondNotificationUseCase {
    void respondToNotification(UUID notificationId, NotificationStatus status, String respondedBy);
}
