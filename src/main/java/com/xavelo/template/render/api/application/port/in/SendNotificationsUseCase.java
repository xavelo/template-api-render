package com.xavelo.template.render.api.application.port.in;

import java.util.UUID;

public interface SendNotificationsUseCase {
    void sendNotifications(UUID authorizationId);
}
