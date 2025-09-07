package com.xavelo.template.render.api.application.port.in;

import java.util.UUID;

public interface MarkNotificationSentUseCase {
    void markNotificationSent(UUID notificationId);
}
